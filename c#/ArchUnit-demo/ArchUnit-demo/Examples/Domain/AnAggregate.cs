using System;
namespace ArchUnit.Demo.Examples.Domain
{
    public class AnAggregate
    {
        private readonly Person person;

        public AnAggregate(Person person)
        {
            this.person = person;
        }
    }
}