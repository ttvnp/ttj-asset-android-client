package com.ttvnp.ttj_asset_android_client.data.translator

import java.util.ArrayList

internal abstract class BaseTranslator<Model, Entity> {

    internal abstract fun translate(entity: Entity?): Model?

    internal fun translate(collection: Collection<Entity>): List<Model> {
        val list = ArrayList<Model>()
        var model: Model?
        collection.forEach {
            translate(it)?.let {
                list.add(it)
            }
        }
        return list
    }
}
