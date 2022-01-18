using System;
namespace ArchUnit.Demo.Examples.Domain
{
    public record Person(string Name, IShittyInterface i) { }
}