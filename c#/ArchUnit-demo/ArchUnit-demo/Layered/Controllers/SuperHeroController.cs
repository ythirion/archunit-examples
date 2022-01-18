using System;
using ArchUnit.Demo.Layered.Repositories;
using ArchUnit.Demo.Layered.Services;

namespace ArchUnit.Demo.Layered.Controllers
{
    public class SuperHeroController
    {
        private readonly SuperHeroService superHeroService;
        private readonly ISuperHeroRepository superHeroRepository;

        public SuperHeroController(SuperHeroService superHeroService,
            ISuperHeroRepository superHeroRepository)
        {
            this.superHeroService = superHeroService;
            this.superHeroRepository = superHeroRepository;
        }
    }
}