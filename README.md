# Jiving Janko Peripherals

A mod that allows grouping of peripherals.

So, like, we were trying to make a disco floor, and we realized that using Project Red for the lights was too laggy due 
to light updates every time a light was turned off or on, and there were latency issues due to how bundled cable handles
addition and subtraction of signal in its ComputerCraft API. We wrote a mod with the purpose of giving us:

1. A shorthand name of every peripheral
2. A grouping system to forward a method call to multiple peripherals at once
3. The ability to assign multiple groups to a peripheral

But then one might ask: how can you easily forward method callsto a group of peripherals, or ngive them a shorthand name? This
is where the mod comes in. It adds a block called a peripheral relay. This block contains NBT data that stores the name and its
groups.

LICENSE INFO

It's open source. So whatever.
