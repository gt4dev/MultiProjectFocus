package gtr.hotest.variants

import gtr.hotest.hotest
import kotlin.test.Test
import kotlin.test.assertEquals

class VariantsTest {

    @Test
    fun `variants one level - classic case`() {
        val result = mutableListOf<String>()
        hotest {
            result.add("start")
            variants("vsA") {
                variant("vsA-v1") { result.add("vsA-v1") }
                variant("vsA-v2") { result.add("vsA-v2") }
                variant("vsA-v3") { result.add("vsA-v3") }
            }
            result.add("end")
        }

        val expected = listOf(
            // loop 1
            "start", "vsA-v1", "end",
            // loop 2
            "start", "vsA-v2", "end",
            // loop 3
            "start", "vsA-v3", "end",
        )
        assertEquals(expected, result)
    }

    @Test
    fun `variants one level - empty variants`() {
        val result = mutableListOf<String>()
        hotest {
            result.add("start")
            variants("vsA") {
                result.add("vsA")
            }
            result.add("end")
        }

        val expected = listOf(
            // loop 1
            "start", "vsA", "end",
        )
        assertEquals(expected, result)
    }

    @Test
    fun `variants two levels - classic variants tree`() {
        val result = mutableListOf<String>()
        hotest {
            result.add("start")
            variants("vsA") {
                variant("vsA-v1") {
                    variants("vsB") {
                        variant("vsB-v1") {
                            result.add("vsB-v1")
                        }
                        variant("vsB-v2") {
                            result.add("vsB-v2")
                        }
                    }
                }
                variant("vsA-v2") {
                    variants("vsC") {
                        variant("vsC-v1") {
                            result.add("vsC-v1")
                        }
                        variant("vsC-v2") {
                            result.add("vsC-v2")
                        }
                    }
                }
            }
            result.add("end")
        }

        val expected = listOf(
            // loop 1
            "start", "vsB-v1", "end",
            // loop 2
            "start", "vsB-v2", "end",
            // loop 3
            "start", "vsC-v1", "end",
            // loop 4
            "start", "vsC-v2", "end",
        )
        assertEquals(expected, result)
    }

    @Test
    fun `variants two levels - variants tree with one child`() {
        val result = mutableListOf<String>()
        hotest {
            result.add("start")
            variants("vsA") {
                variant("vsA-v1") {
                    variants("vsB") {
                        variant("vsB-v1") {
                            result.add("vsB-v1")
                        }
                    }
                }
                variant("vsA-v2") {
                    variants("vsC") {
                        variant("vsC-v1") {
                            result.add("vsC-v1")
                        }
                    }
                }
            }
            result.add("end")
        }

        val expected = listOf(
            // loop 1
            "start", "vsB-v1", "end",
            // loop 2
            "start", "vsC-v1", "end",
        )
        assertEquals(expected, result)
    }


    @Test()
    fun `precise travers through variants tree`() {
        val result = mutableListOf<String>()
        hotest {
            result.add("start")
            variants("vsA") {
                result.add("vsA-start")
                variant("vsA-v1") {
                    result.add("vsA-v1-start")
                    variants("vsB") {
                        result.add("vsB-start")
                        variant("vsB-v1") {
                            result.add("vsB-v1")
                        }
                        result.add("vsB-mid")
                        variant("vsB-v2") {
                            result.add("vsB-v2")
                        }
                        result.add("vsB-end")
                    }
                    result.add("vsA-v1-end")
                }
                result.add("vsA-mid")
                variant("vsA-v2") {
                    result.add("vsA-v2-start")
                    variants("vsC") {
                        result.add("vsC-start")
                        variant("vsC-v1") {
                            result.add("vsC-v1")
                        }
                        result.add("vsC-mid")
                        variant("vsC-v2") {
                            result.add("vsC-v2")
                        }
                        result.add("vsC-end")
                    }
                    result.add("vsA-v2-end")
                }
                result.add("vsA-end")
            }
            result.add("end")
        }

        // distilled view on variants tree
        // variants("vsA") {
        //     variant("vsA-v1") {
        //         variants("vsB") {
        //             variant("vsB-v1") { }
        //             variant("vsB-v2") { }
        //         }
        //     }
        //     variant("vsA-v2") {
        //         variants("vsC") {
        //             variant("vsC-v1") { }
        //             variant("vsC-v2") { }
        //         }
        //     }
        // }

        // @formatter:off
        val expected = listOf(
            // loop 1
             "start", "vsA-start", "vsA-v1-start", "vsB-start", "vsB-v1", "vsB-mid", "vsB-end", "vsA-v1-end", "vsA-mid", "vsA-end", "end",
            // loop 2
             "start", "vsA-start", "vsA-v1-start", "vsB-start", "vsB-mid", "vsB-v2", "vsB-end", "vsA-v1-end", "vsA-mid", "vsA-end", "end",
            // loop 3
             "start", "vsA-start", "vsA-mid", "vsA-v2-start", "vsC-start", "vsC-v1", "vsC-mid", "vsC-end", "vsA-v2-end", "vsA-end", "end",
            // loop 4
             "start", "vsA-start", "vsA-mid", "vsA-v2-start", "vsC-start", "vsC-mid", "vsC-v2", "vsC-end", "vsA-v2-end", "vsA-end", "end",
        )
        // @formatter:on
        assertEquals(expected, result)
    }
}