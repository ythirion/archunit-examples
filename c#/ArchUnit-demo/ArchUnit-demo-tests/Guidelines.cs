using ArchUnit.Demo.Examples;
using Microsoft.AspNetCore.Mvc;
using Xunit;
using static ArchUnitNET.Fluent.ArchRuleDefinition;
using static ArchUnitNET.Fluent.Slices.SliceRuleDefinition;

namespace ArchUnit.Demo.Tests
{
    public class Guidelines
    {
        [Fact]
        public void ClassShouldNotDependOnAnother() =>
            Classes().That()
                .Are(typeof(SomeExample))
                .Should()
                .NotDependOnAny(typeof(Other))
                .Check();

        [Fact]
        public void AnnotatedClassesShouldResideInAGivenNamespace() =>
            Classes().That()
                .HaveAnyAttributes(typeof(ApiControllerAttribute))
                .Should()
                .ResideInNamespace("Controllers")
                .Check();

        [Fact]
        public void ServiceSliceHasNoCycleDependencies() =>
            Slices()
                .Matching("*.Modules.(*)").Should()
                .BeFreeOfCycles()
                .Check();

        [Fact]
        public void InterfacesShouldStartWithI() =>
            Interfaces().Should()
                .HaveNameMatching("^I[A-Z].*", useRegularExpressions: true)
                .Because("C# convention...")
                .Check();

        [Fact]
        public void ClassesInDomainCanOnlyAccessClassesInDomainItself() =>
            Classes().That()
                .ResideInNamespace("Domain")
                .Should()
                .OnlyDependOnTypesThat()
                .ResideInNamespace("Domain")
                .Check();
    }
}