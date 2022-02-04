using System.Diagnostics.CodeAnalysis;

namespace ArchUnit.Demo.Examples
{
    [ExcludeFromCodeCoverage]
    public class DecoratedClass
    {
        [ExcludeFromCodeCoverage]
        public void AmAnnotated()
        {
        }
    }
}