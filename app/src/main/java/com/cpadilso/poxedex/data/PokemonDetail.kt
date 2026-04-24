package com.cpadilso.poxedex.data

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: Sprites,
    val types: List<TypeSlot>,
    val stats: List<Stat>,
    val abilities: List<AbilitySlot>
)

data class Sprites(
    val front_default: String?,
    val other: Other?
)

data class Other(
    val official_artwork: OfficialArtwork?
)

data class OfficialArtwork(
    val front_default: String?
)

data class TypeSlot(
    val type: Type
)

data class Type(
    val name: String
)

data class Stat(
    val base_stat: Int,
    val stat: StatName
)

data class StatName(
    val name: String
)

data class AbilitySlot(
    val ability: Ability
)

data class Ability(
    val name: String
)
