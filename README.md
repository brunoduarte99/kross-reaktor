# Kross Reaktor

A cross-platform shared service built to standardize and centralize reusable features across all platforms.

---

## Overview

This service provides a set of reusable modules that can be consumed by any platform within the ecosystem, ensuring consistency and avoiding duplication of logic across projects.

---

## User Preferences

### Table Definitions
Manages the registration of tables and their available column keys. Acts as the source of truth for what columns exist per table, decoupling column management from individual platform deployments.

### Column Config
Manages per-user column visibility and ordering for any registered table. Automatically initializes default configurations based on the registered table definitions.

### Filter Groups
Manages per-user saved filter combinations for any registered table. Allows users to save, name, and reuse filter presets across sessions.

---

## Planned Modules

### Crypto Shredding
Standardized cryptographic key management to support data shredding across platforms. Ensures consistent handling of encryption and key lifecycle.

### Zones
Centralized management of the geographic and operational zones the company operates in. Provides a single source of truth for zone definitions reused across platforms.
