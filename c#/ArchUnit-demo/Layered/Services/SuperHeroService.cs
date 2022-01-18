using System;
using ArchUnit.Demo.Layered.Repositories;

namespace ArchUnit.Demo.Layered.Services
{
    public class SuperHeroService
    {
        private readonly ISuperHeroRepository superHeroRepository;

        public SuperHeroService(ISuperHeroRepository superHeroRepository)
        {
            this.superHeroRepository = superHeroRepository;
        }
    }
}