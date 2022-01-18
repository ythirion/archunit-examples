using System;
using ArchUnit.Demo.Modules.Module2;

namespace ArchUnit.Demo.Modules.Module1
{
    public class A
    {
        private readonly B b;

        public A(B b)
        {
            this.b = b;
        }
    }
}