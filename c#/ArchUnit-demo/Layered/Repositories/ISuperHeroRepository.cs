using System;
namespace ArchUnit.Demo.Layered.Repositories
{
    public interface ISuperHeroRepository
    {
        Task<SuperHeroEntity> Save(SuperHeroEntity superHero);
    }
}