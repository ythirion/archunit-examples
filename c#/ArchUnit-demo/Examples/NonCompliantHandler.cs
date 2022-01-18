using System;
using ArchUnit.Demo.Examples.Commands;

namespace ArchUnit.Demo.Examples
{
    public class NonCompliantHandler : ICommandHandler<Order>
    {
        public int Handle(Order command) => 42;
    }
}