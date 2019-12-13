# Renminbi

Renminbi is an open-source 1.8.x chat logging mod. It keeps track of everything that touches the chat box
and stores it locally in XML-formatted logs. Alongside every message stored is a timestamp, kept
to the second in local time.

## Renminbi logs aren't huge

A 1MB Renminbi log file would represent about 7 hours of straight gameplay on a highly active server.

If that's still a little too big for you, logging can be disabled at any time with `/rmb toggle`

## Renminbi is open source

Surely you couldn't be comfortable with a program that's designed to keep data having its internals obscured.
For that reason, Renminbi and [Renminbi View](https://github.com/t1ra/renminbi-view) are hosted for the 
public eye on GitHub.

## Renminbi is easy to use.

Renminbi stars logging as soon as you join a server, and stops when you close the game.

If you want to know if you're logging or not, and where you're logging to, you can use `/rmb status`

There's also `/rmb help`, in case the documentation is missing anything.

## FAQ

### Why the name?

**R**e**m**em**b**er (messages) -> RMB -> **R**en**m**in**b**i

### Why?

Why not?

## Building

Like any other Forge mod, hopefully.

```
gradlew setupDecompWorkspace

gradlew eclipse

gradlew build
```

## License

LGPLv3 because its what everyone else is using and I'm scared to use something more permissive :(((
Even though that's what LGPL is for.
