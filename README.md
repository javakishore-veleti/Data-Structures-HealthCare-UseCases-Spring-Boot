# Data-Structures-Alrogithms (DSA) & Use Cases For Healthcare Plans Qualification and Order Management – Java Spring Boot Application
A real-world Spring Boot Java project showcasing 12 core DSA patterns applied to healthcare plan and order management use cases. Each pattern is implemented with domain-specific context using realistic product and account data structures.

## Overview

This application serves a dual purpose:

1. **Healthcare Plans Management Platform**  
   A hands-on Java Spring Boot backend that models real-world healthcare insurance plans, including:
    - Product and product feature catalogs
    - Dynamic pricing and discounting models
    - Qualification logic based on `country`, `county`, `channel`, and `account_number`
    - Order lifecycle (AccountPlans, Orders, OrderLines)

2. **DSA (Data Structures and Algorithms) Mastery Platform**  
   Integrated into the application is a curated implementation of **12 core DSA patterns** (from the DSA Cheatsheet) across all major algorithm types (arrays, strings, trees, graphs, DP, etc.).  
   Each DSA pattern is paired with a realistic **business use case** from the healthcare domain, allowing for applied learning and GitHub portfolio building.

## Core Purpose

This application is built for senior/principal engineers or architect-level developers who:

- Want to apply DSA patterns in practical, domain-rich enterprise scenarios.
- Are preparing for system design or algorithm-heavy interviews.
- Intend to showcase real-world, business-relevant problem-solving using foundational DSA knowledge.

## Healthcare Domain Models

The system models a full healthcare product and plan management system:

- **Product**: Each insurance offering
- **ProductFeature**: Features such as coverage, benefits
- **ProductPrice / Discount / Qualification**: Dynamic and rule-based
- **AccountPlans**: Plans purchased by accounts
- **AccountPlanOrder / OrderLine**: Complete order management workflow

All entities are managed via JPA, with a local H2 profile for development and PostgreSQL for production.

## DSA Patterns and Mapped Use Cases

| DSA Algorithm Pattern         | Pattern Subtype                         | Healthcare Business Use Case                                                                 |
|------------------------------|-----------------------------------------|----------------------------------------------------------------------------------------------|
| Arrays and Strings           | Sliding Window                          | Analyze plan usage in moving time windows (e.g., last 7 days)                               |
|                              | Two Pointers                            | Compare plan tiers or eligibility conditions                                                 |
|                              | Prefix Sum                              | Calculate cumulative discount or total cost projections                                     |
|                              | In-place Operations                     | Optimize transformation of account-level eligibility flags                                  |
| Hashing                      | Frequency Count                         | Count frequency of plan types per region or user group                                      |
|                              | Map + Sliding Window                    | Track repeated plan usage within time windows                                                |
|                              | Grouping                                | Group account orders by status or plan tiers                                                 |
|                              | Anagrams                                | Match identical feature sets in different plan names                                         |
| Trees and Binary Search Trees| BFS                                     | Traverse plan-feature trees to determine eligibility                                        |
|                              | DFS                                     | Drill down qualification paths for plan-rule trees                                           |
|                              | In-order / Pre-order / Post-order       | Map feature inheritance or override ordering in plan trees                                   |
| Recursion and Backtracking   | Permutation                             | Find valid combinations of plan features (e.g., discount + price limit + region rule)       |
|                              | Combination                             | Combine qualification rules to find eligible plans                                           |
|                              | Exploration (DFS)                       | Explore feature sets or custom plan builds                                                   |
| Binary Search                | Standard Binary Search                  | Search plans by price or priority                                                           |
|                              | Binary Search on Answer                 | Determine max budget under which plans are still eligible                                   |
| Stacks and Queues            | Monotonic Stack                         | Track trending plans (costs or popularity rising/falling)                                   |
|                              | Next Greater Element                    | Predict which plan upgrade to recommend next                                                |
|                              | Simulate Queue                          | Simulate plan order processing flow                                                          |
|                              | Recursion                               | Handle recursive rule evaluation for plan qualification                                     |
| Linked Lists                 | Fast/Slow Pointers                      | Detect loops in plan lifecycle (re-activation, suspension cycles)                           |
|                              | Cycle Detection                         | Catch circular dependencies in product-feature combinations                                 |
|                              | Reversal                                | Undo operations or rollback plan orders                                                     |
| Priority Queues              | Kth Largest Element                     | Get top-N discounted plans or user-preferred choices                                        |
|                              | Merge K Sorted Lists                    | Merge plan lists sorted by region or eligibility score                                      |
| Graphs                       | Topological Sort                        | Enforce feature enablement order (e.g., pricing after base plan)                            |
|                              | DFS / BFS                               | Determine connected qualification dependencies                                              |
|                              | Union Find                              | Cluster related plans with shared qualifications                                            |
| Dynamic Programming          | 0/1 Knapsack                            | Maximize value (features) under user’s cost constraint                                      |
|                              | Memoization                             | Cache eligibility outcomes per user or region                                               |
|                              | Tabulation (bottom-up)                  | Build eligibility tables for plans based on rules                                           |
| Advanced Data Structures     | Tries                                   | Auto-complete plan features or fast search                                                  |
|                              | Bit manipulation in sets                | Feature flags encoded as bits for performance-efficient checks                              |
| Bit Manipulation             | Tricks                                  | Use flags to represent binary eligibility states                                            |
|                              | Masking                                 | Bitmask permissions (e.g., eligibility criteria combinations)                               |
|                              | AND / OR / NOT operations               | Apply logic filters across product features or account rules                                |


### What is “Topological Sort”?
- Topological sort is an algorithm that orders nodes of a Directed Acyclic Graph (DAG) such that for every directed edge U → V, node U comes before V in the ordering.
- Think of it as : Sort tasks based on their dependencies.
- Real-World Analogy (especially for this healthcare or AWS Services test data use case (look at CSV file in src/main/resources folder)):
  - Imagine you’re deploying AWS services, but:
    - IAM must be set up before EC2, 
    - EC2 must be enabled before you attach EBS, 
    - EBS must be attached before backups start. 
    - Topological sort will give you an order like: IAM → EC2 → EBS → Backup
    - So you never try to enable something before its prerequisites are satisfied.
- Why is it called a “Sort”?
  - In typical sorting, you order items by magnitude or priority (like numbers or strings). In topological sort, you’re ordering by dependency — who needs to come before whom. 
  - So it’s not sorting values, but sorting positions in a dependency chain. 
  - It’s also not unique — multiple valid topological orderings may exist.

- Key Properties: 

| Concept        | Explanation                                                                                                      |
| -------------- | ---------------------------------------------------------------------------------------------------------------- |
| **Applies To** | Directed Acyclic Graphs (DAGs) only                                                                              |
| **Input**      | Nodes and directed edges (dependencies)                                                                          |
| **Output**     | A linear ordering of nodes respecting dependency constraints                                                     |
| **Use Cases**  | Build systems (Maven, Gradle), CI/CD pipelines, feature enablement, course prerequisite planning, job scheduling |

- How It Relates to This Code 
  - In GraphDependencyServiceImpl:
    - We collected the dependency edges for a product (e.g., IAM → EC2), 
    - Built an adjacency list + in-degree map, 
    - Ran Kahn's algorithm to return a valid topological ordering.
    
- What Happens If There’s a Cycle? 
  - Topological sorting fails — because there's no valid linear order to satisfy circular dependencies. 
  - That’s why  resp.setMessage("Cycle detected...") is triggered when bad data sneaks in.


## Tech Stack

- Java 21 and Spring Boot 3.2
- JPA with H2 (dev) and PostgreSQL (prod)
- Liquibase for database migrations
- Lombok for boilerplate reduction
- Maven build
- Structured DSA module implementation inside domain-driven packages

