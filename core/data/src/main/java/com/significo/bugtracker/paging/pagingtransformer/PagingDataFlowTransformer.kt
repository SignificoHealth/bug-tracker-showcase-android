package com.significo.bugtracker.paging.pagingtransformer

import androidx.paging.PagingData
import com.significo.bugtracker.paging.pagingtransformer.transformations.EditItem
import com.significo.bugtracker.paging.pagingtransformer.transformations.InsertFooterItem
import com.significo.bugtracker.paging.pagingtransformer.transformations.InsertHeaderItem
import com.significo.bugtracker.paging.pagingtransformer.transformations.RemoveItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

/**
 * Represents an abstraction layer that allows us to "edit" the Flow from the Pager class used by Jetpack Paging v3.
 *
 * It's important to remember that clearTransformationsList needs to be called manually every time the adapter is
 * refreshed, which unfortunately there is no automatic way of doing it.
 *
 * Another important aspect of this solution is that it might not scale too well for screens that have a huge number of
 * items and requires a lot of updates, as each update will make the list to be "rebuilt".
 *
 * The solution was based on the solution proposed by Google on the following issue:
 * https://issuetracker.google.com/issues/186782655#comment5
 *
 * This solution can be improved when Kotlin starts supporting suspend functions as super types, which could enable
 * us to make this class extend `suspend FlowCollector<R>.(T) -> Unit` and use the transform operator to apply the
 * changes.
 *
 * For more information check the following links:
 * https://youtrack.jetbrains.com/issue/KT-18707
 * https://kotlinlang.org/docs/flow.html#transform-operator
 * https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/transform.html
 *
 * @param ContentType Paginated list items type.
 */
class PagingDataFlowTransformer<ContentType : Any> {
    private val transformationsFlow = MutableStateFlow<List<PagingDataTransformation<ContentType>>>(emptyList())

    /**
     * Combines the transformationsFlow with the flow exposed by a Pager, allowing us to "modify" its content.
     *
     * @param flow The flow exposed by a Pager object.
     */
    fun bind(flow: Flow<PagingData<ContentType>>): Flow<PagingData<ContentType>> =
        flow.combine(transformationsFlow) { pagingData, transformations ->
            transformations.fold(pagingData) { accumulatedPagingData, transformation ->
                transformation.apply(accumulatedPagingData)
            }
        }

    /**
     * Cleans the transformations list that will be applied to the PagingData. This method must be called every time
     * the adapter is refreshed.
     */
    fun clearTransformationsList() {
        transformationsFlow.value = emptyList()
    }

    private fun performTransformation(pagingDataTransformation: PagingDataTransformation<ContentType>) {
        transformationsFlow.value += pagingDataTransformation
    }

    /**
     * Removes items from the paginated list by filtering them out.
     *
     * @param itemsMatcher Matcher function that must returns true when item should be filtered out from the resulting
     * paged list.
     */
    fun removeItems(itemsMatcher: (item: ContentType) -> Boolean) = performTransformation(RemoveItems(itemsMatcher))

    /**
     * Edits items on the list by performing the map function only to items that make the itemMatcher function return
     * true.
     *
     * @param itemMatcher Matcher function that must return true when the item to be edited is found.
     * @param itemTransformer Transformation function that receives the item that needs to be changed and returns the
     * result of a transformation.
     */
    fun editItem(
        itemMatcher: (item: ContentType) -> Boolean,
        itemTransformer: (itemToBeChanged: ContentType) -> ContentType
    ) = performTransformation(EditItem(itemMatcher, itemTransformer))

    /**
     * Inserts an item at the beginning of the paged list.
     *
     * @param newItem The new item to be inserted.
     */
    fun insertHeaderItem(newItem: ContentType) = performTransformation(InsertHeaderItem(newItem))

    /**
     * Inserts an item at the end of the paged list.
     *
     * @param newItem The new item to be inserted.
     */
    fun insertFooterItem(newItem: ContentType) = performTransformation(InsertFooterItem(newItem))
}
