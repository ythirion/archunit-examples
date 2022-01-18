using System;
using ArchUnit.Demo.Modules.Module1;

namespace ArchUnit.Demo.Modules.Module2
{
    public class B
    {
        private A CreateA() => new A(this);
    }
}