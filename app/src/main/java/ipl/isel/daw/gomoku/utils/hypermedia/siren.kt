package ipl.isel.daw.gomoku.utils.hypermedia

import okhttp3.MediaType.Companion.toMediaType

/**
 * For details regarding the Siren media type, see <a href="https://github.com/kevinswiber/siren">Siren</a>
 */
val SirenMediaType = "application/vnd.siren+JSON".toMediaType()
val ApplicationJsonType = "application/problem+json".toMediaType()
val OrdinaryJsonType = "application/json".toMediaType()


/*
 * Gets a Siren self link for the given URI
 *
 * @param uri the string with the self URI
 * @return the resulting siren link
 */
//fun selfLink(uri: String) = SirenLink(rel = listOf("self"), href = URI(uri))

/**
 * Class whose instances represent links as they are represented in Siren.
 */
/*
data class SirenLink(
    val rel: List<String>,
    val href: URI,
    val title: String? = null,
    val type: MediaType? = null)
*/

/**
 * Class whose instances represent actions that are included in a siren entity.
 */
/*data class SirenAction(
    val name: String,
    val href: URI,
    val title: String? = null,
    @SerializedName("class")
    val clazz: List<String>? = null,
    val method: String? = null,
    val type: String? = null,
    val fields: List<Field>? = null
) {
    *//**
     * Represents action's fields
     *//*
    data class Field(
        val name: String,
        val type: String? = null,
        val value: String? = null,
        val title: String? = null
    )
}*/

/*data class SirenEntity<T>(
    @SerializedName("class") val clazz: List<String>? = null,
    val properties: List<T> = emptyList(),
    val entities: List<SubEntity>? = null,
    val links: List<SirenLink>? = null,
    val actions: List<SirenAction>? = null,
    val title: String? = null
) {
    companion object {
        inline fun <reified T> getType(): TypeToken<SirenEntity<T>> =
            object : TypeToken<SirenEntity<T>>() { }
    }
}*/

/*
 * Base class for admissible sub entities, namely, [EmbeddedLink] and [EmbeddedEntity].
 * Notice that this is a closed class hierarchy.
 */
/*sealed class SubEntity*/

/*data class EmbeddedLink(
    @SerializedName("class")
    val clazz: List<String>? = null,
    val rel: List<String>,
    val href: URI,
    val type: MediaType? = null,
    val title: String? = null
) : SubEntity()*/

/*data class EmbeddedEntity<T>(
    val rel: List<String>,
    @SerializedName("class") val clazz: List<String>? = null,
    val properties: T? =null,
    val entities: List<SubEntity>? = null,
    val links: List<SirenLink>? = null,
    val actions: List<SirenAction>? = null,
    val title: String? = null
) : SubEntity() {
    companion object {
        inline fun <reified T> getType(): TypeToken<EmbeddedEntity<T>> =
            object : TypeToken<EmbeddedEntity<T>>() { }
    }
}*/


/**
 * Gson deserializer for the SubEntity sum type
 */
/*class SubEntityDeserializer<T>(private val propertiesType: Type) : JsonDeserializer<SubEntity> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): SubEntity {

        val entity = json.asJsonObject
        val entityPropertiesMember = "properties"
        return if (entity.has(entityPropertiesMember)) {
            val item = context.deserialize<T>(
                entity.getAsJsonObject(entityPropertiesMember),
                propertiesType
            )
            EmbeddedEntity(
                rel = entity.getAsListOfString("rel") ?: emptyList(),
                clazz = entity.getAsListOfString("class"),
                properties = item,
                links = entity.getAsListOf("links", SirenLink::class.java, context),
                actions = entity.getAsListOf("actions", SirenAction::class.java, context),
                title = entity.get("title")?.asString
            )
        }
        else {
            context.deserialize(entity, EmbeddedLink::class.java)
        }
    }
}*/

/*
private fun JsonObject.getAsListOfString(propertyName: String): List<String>? =
    getAsJsonArray(propertyName)?.map { it.asString }

private fun <T> JsonObject.getAsListOf(
    propertyName: String,
    type: Class<T>,
    context: JsonDeserializationContext
): List<T>? =
    getAsJsonArray(propertyName)?.map {
        context.deserialize<T>(it, type)
    }
*/

