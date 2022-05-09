using ArchUnit.Demo.Layered.Controllers;
using ArchUnitNET.Domain;
using ArchUnitNET.Fluent;
using ArchUnitNET.Fluent.Syntax.Elements.Types;
using ArchUnitNET.Loader;
using ArchUnitNET.xUnit;
using static ArchUnitNET.Fluent.ArchRuleDefinition;

namespace ArchUnit.Demo.Tests
{
    public static class ArchUnitExtensions
    {
        public static GivenTypesConjunction TypesInAssembly() =>
            Types().That().Are(Architecture.Types);

        private static readonly Architecture Architecture =
            new ArchLoader()
                .LoadAssemblies(typeof(SuperHeroController).Assembly)
                .Build();

        public static void Check(this IArchRule rule) => rule.Check(Architecture);
    }
}