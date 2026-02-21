package gtr.mpfocus.infra.db_repo

// use here `expect` because Android version of `Room.inMemoryDatabaseBuilder(..)` requires Android's Context
// remaining platforms just use 'cross-platform' API
internal expect fun createInMemoryTestDb(): MPFDatabase
