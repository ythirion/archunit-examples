using System;

namespace ArchUnit.Demo.Layered.Repositories
{
    public record SuperHeroEntity(Guid Id, string Name, IReadOnlyList<string> Powers) {}
}