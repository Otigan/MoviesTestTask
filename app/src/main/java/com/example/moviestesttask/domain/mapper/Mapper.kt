package com.example.moviestesttask.domain.mapper

interface Mapper<Model, Entity> {

    fun toEntity(model: Model): Entity

    fun toModel(entity: Entity): Model
}