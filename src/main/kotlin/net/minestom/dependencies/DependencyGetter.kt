package net.minestom.dependencies

import net.minestom.dependencies.maven.MavenRepository
import net.minestom.dependencies.maven.MavenResolver
import java.io.File
import java.nio.file.Path

class DependencyGetter {

    private val resolverList = mutableListOf<DependencyResolver>()

    fun addResolver(resolver: DependencyResolver) = apply { resolverList += resolver }
    fun addResolvers(vararg resolvers: DependencyResolver) = apply { resolverList.addAll(resolvers) }

    /**
     * Shorthand to add a MavenResolver with the given repositories
     *
     * @param repositories The repositories to add
     */
    fun addMavenResolver(repositories: List<MavenRepository>) = addResolver(MavenResolver(repositories))

    /**
     * Shorthand to add a MavenResolver with the given repositories
     *
     * @param repositories The repositories to add
     */
    fun addMavenResolver(vararg repositories: MavenRepository) = addMavenResolver(repositories.toList())

    fun get(id: String, targetFolder: Path): ResolvedDependency =
        resolverList.firstNotNullOfOrNull { it.resolveOrNull(id, targetFolder) }
            ?: throw UnresolvedDependencyException("Could not find $id inside resolver list: ${resolverList.joinToString { it.toString() }}")

}