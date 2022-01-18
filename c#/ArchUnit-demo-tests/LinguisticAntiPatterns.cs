using ArchUnitNET.Fluent.Syntax.Elements.Members.MethodMembers;
using Xunit;
using static ArchUnitNET.Fluent.ArchRuleDefinition;

namespace ArchUnit.Demo.Tests
{
    public class LinguisticAntiPatterns
    {
        private static GivenMethodMembersThat Methods() => MethodMembers().That().AreNoConstructors().And();

        [Fact]
        public void NoGetMethodShouldReturnVoid() =>
            Methods()
                .HaveNameMatching("Get[A-Z].*", useRegularExpressions: true).Should()
                .NotHaveReturnType(typeof(void))
                .Check();

        [Fact]
        public void IserAndHaserShouldReturnBooleans() =>
            Methods()
                .HaveNameMatching("Is[A-Z].*", useRegularExpressions: true).Or()
                .HaveNameMatching("Has[A-Z].*", useRegularExpressions: true).Should()
                .HaveReturnType(typeof(bool))
                .Check();

        [Fact]
        public void SettersShouldNotReturnSomething() =>
            Methods()
                .HaveNameMatching("Set[A-Z].*", useRegularExpressions: true).Should()
                .HaveReturnType(typeof(void))
                .Check();
    }
}