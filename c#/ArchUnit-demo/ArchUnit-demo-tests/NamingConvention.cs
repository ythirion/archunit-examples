using ArchUnit.Demo.Examples;
using Xunit;
using static ArchUnitNET.Fluent.ArchRuleDefinition;
using static ArchUnit.Demo.Tests.Demo;

namespace ArchUnit.Demo.Tests
{
    public class NamingConvention
    {
        [Fact]
        public void ServicesShouldBeSuffixedByService() =>
            Classes()
                .That()
                .ResideInNamespace("Services").Should()
                .HaveNameEndingWith("Service")
                .Check();

        [Fact]
        public void CommandHandlersShouldBeSuffixedByCommandHandler() =>
            Classes()
                .That()
                .ImplementInterface(typeof(ICommandHandler<>)).Should()
                .HaveNameEndingWith("CommandHandler")
                .Check();
    }
}

