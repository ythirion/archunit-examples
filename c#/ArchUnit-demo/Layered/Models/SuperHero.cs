using System;

namespace ArchUnit.Demo.Layered.Models
{
    public record SuperHero(Guid Id, string Name, IReadOnlyList<string> Powers) { }
}