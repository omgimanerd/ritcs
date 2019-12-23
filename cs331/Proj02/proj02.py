#!/usr/bin/env python3

import random

HAWK = 'Hawk'
DOVE = 'Dove'
DEAD = 'DEAD'

"""
Class encapsulating an individual member of the population.
"""
class Individual():
    def __init__(self, number, status, resource=0):
        self.number = number
        self.status = status
        self.resource = resource

    def __eq__(self, status):
        assert type(status) is str
        return self.status == status

    def __iadd__(self, resource):
        self.resource += resource
        return self

    def __isub__(self, resource):
        self.resource -= resource
        if self.isDead():
            self.status = DEAD
        return self

    def isAlive(self):
        return self.resource >= 0
        
    def isDead(self):
        return self.resource < 0

    def getStatusString(self):
        return 'Individual {}: {}'.format(self.number, self.status)

    def getResourceString(self):
        return 'Individual {}={}'.format(self.number, self.resource)

    def getDisplayString(self):
        return 'Individual[{}]={}:{}'.format(self.number, self.status, self.resource)

"""
Class encapsulating the simulation state.
"""
class Simulation:
    def __init__(self, popSize, percentHawks=20, resourceAmt=50, costHawkHawk=100):
        self.popSize = int(popSize)
        self.percentHawks = int(percentHawks)
        self.resourceAmt = int(resourceAmt)
        self.costHawkHawk = int(costHawkHawk)

        self.population = []
        self.encounter = 1
        self.setup()

    def setup(self):
        numHawks = int(self.popSize * (self.percentHawks / 100))
        numDoves = self.popSize - numHawks
        for i in range(self.popSize):
            self.population.append(
                Individual(i, HAWK) if i < numHawks else Individual(i, DOVE))

    def step(self):
        alive = [individual for individual in self.population if individual.isAlive()]
        individuals = random.sample(alive, 2)
        print('Encounter: {}'.format(self.encounter))
        print(individuals[0].getStatusString())
        print(individuals[1].getStatusString())
        if individuals[0] == DOVE and individuals[1] == DOVE:
            amount = self.resourceAmt // 2
            individuals[0] += amount
            individuals[1] += amount
            print('Dove/Dove: Dove: {}    Dove: {}'.format(amount))
        elif individuals[0] == HAWK and individuals[1] == HAWK:
            cost = self.resourceAmt - self.costHawkHawk
            individuals[0] += cost
            individuals[1] -= self.costHawkHawk
            print('Hawk/Hawk: Hawk: {}    Hawk: {}'.format(cost, self.costHawkHawk))
            if individuals[0].isDead():
                print('Hawk one has died!')
            if individuals[1].isDead():
                print('Hawk two has died!')
        elif individuals[0] == HAWK and individuals[1] == DOVE:
            individuals[0] += self.resourceAmt
            print('Hawk/Dove: Hawk: {}    Dove: {}'.format(self.resourceAmt, 0))
        elif individuals[0] == DOVE and individuals[1] == HAWK:
            individuals[1] += self.resourceAmt
            print('Dove/Hawk: Dove: {}    Hawk: {}'.format(0, self.resourceAmt))
        else:
            raise RuntimeError('Uh oh! This should never happen!')
        print('{}\t{}'.format(individuals[0].getResourceString(),
                              individuals[1].getResourceString()))
        self.encounter += 1

    def __str__(self):
        return "\n".join(map(str, self.population))

if __name__ == '__main__':
    import sys
    if len(sys.argv) < 2 or len(sys.argv) > 5:
        print('Usage: ./project02 popSize [percentHawks] [resourceAmt] [costHawk-Hawk]')
    s = Simulation(*sys.argv[1:])
    s.step()
