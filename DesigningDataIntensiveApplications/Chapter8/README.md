# Chapter 8: The Trouble with Distributed Systems

### Problems that can occur in distributed systems

- Whenever you try to send a packet over the network, it may be lost or arbitrarily delayed. Likewise, the reply may be
  lost or delayed, so if you don’t get a reply, you have no idea whether the message got through.
- A node's clock may be significantly out of sync with other nodes (despite your best efforts to set up NTP), it may
  suddenly jump forward or back in time, and relying on it is dangerous because you most likely don’t have a good
  measure of your clock's error interval.
- A process may pause for a substantial amount of time at any point in its execution (perhaps due to a stop-the-world
  garbage collector), be declared dead by other nodes, and then come back to life again without realizing that it was
  paused.

Whenever software tries to do anything involving other nodes, there is the possibility that it may occasionally fail,
or randomly go slow, or not respond at all (and eventually time out).

To tolerate faults, the first step is to _detect_ them.

Major decisions cannot be safely made by a single node, so we require protocols that enlist help from other
nodes and try to get a quorum to agree.

It is possible to give hard real-time response guarantees and bounded delays in networks, but doing so is very
expensive and results in lower utilization of hardware resources.

Supercomputers assume reliable components and thus have to be stopped and restarted entirely when a component fails.
By contrast, distributed systems can run forever without being interrupted at the service level, because all faults and
maintenance can be handled at the node level (at least in theory).
