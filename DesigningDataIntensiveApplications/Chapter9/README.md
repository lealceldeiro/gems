# Chapter 9: Consistency and Consensus

_Linearizability_ is a popular consistency model whose goal is to make replicated data appear as though there were only
a single copy, and to make all operations act on it atomically -- it has the downside of being slow, especially in
environments with large network delays.

_Causality_ imposes an ordering on events in a system (what happened before what, based on cause and effect). It
provides us with a weaker consistency model: some things can be concurrent, so the version history is like a timeline
with branching and merging. Causal consistency does not have the coordination overhead of linearizability and is much
less sensitive to network problems.

However, even if we capture the causal ordering (for example using Lamport timestamps), this implementation has some
limitations (i.e.: if we'd like to ensure that a username is unique -- across all replica nodes -- and reject any
concurrent registrations for the same username, if one node is going to accept a registration, it needs to somehow know
that another node isn't concurrently in the process of registering the same name). This problem could be solved by
following a consensus algorithm.

Achieving consensus means deciding something in such a way that all nodes agree on what was decided, and such decision
is irrevocable.

Some problems that can be solved with _consensus_ are:

- Linearizable compare-and-set registers: The register needs to atomically _decide_ whether to set its value, based on
  whether its current value equals the parameter given in the operation.
- Atomic transaction commit: A database must _decide_ whether to commit or abort a distributed transaction.
- Total order broadcast: The messaging system must _decide_ on the order in which to deliver messages.
- Locks and leases: When several clients are racing to grab a lock or lease, the lock _decides_ which one
  successfully acquired it.
- Membership/coordination service: Given a failure detector (e.g., timeouts), the system must _decide_ which nodes are
  alive, and which should be considered dead because their sessions timed out.
- Uniqueness constraint: When several transactions concurrently try to create conflicting records with the
  same key, the constraint must _decide_ which one to allow and which should fail with a constraint violation.

All of these are straightforward if you only have a single node, or if you are willing to assign the decision-making
capability to a single node. This is what happens in a single-leader database: all the power to make decisions is vested
in the leader, which is why such databases are able to provide linearizable operations, uniqueness constraints, a
totally ordered replication log, and more.

However, if that single leader fails, or if a network interruption makes the leader unreachable, such a system becomes
unable to make any progress. There are three ways of handling that situation:

- Wait for the leader to recover, and accept that the system will be blocked in the meantime.
- Manually fail over by getting humans to choose a new leader node and reconfigure the system to use it.
- Use an algorithm to automatically choose a new leader. This approach requires a consensus algorithm, and it is
  advisable to use a proven algorithm that correctly handles adverse network conditions.

Although a single-leader database can provide linearizability without executing a consensus algorithm on every write, it
still requires consensus to maintain its leadership and for leadership changes.

Tools like ZooKeeper play an important role in providing an “outsourced” consensus, failure detection, and membership
service that applications can use.

Not every system necessarily requires consensus: for example, leaderless and multi-leader replication systems typically
do not use global consensus. The conflicts that occur in these systems are a consequence of not having consensus across
different leaders, but maybe that is okay: maybe we simply need to cope without linearizability and learn to work
better with data that has branching and merging version histories.
