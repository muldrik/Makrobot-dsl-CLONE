package csc.makrobot.dsl

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestNegative {

    @Test
    fun testContext1() {
        assertCompilationFails("""
            import csc.markobot.api.*
            import csc.markobot.dsl.*
            
            fun badContext() {
                робот("Wall-e") {
                    голова {
                        пластик толщиной 2
                        
                        голова {
                            пластик толщиной 2
                        }
                    }
                }
            }
        """)
    }

    @Test
    fun testContext2() {
        assertCompilationFails("""
            import csc.markobot.api.*
            import csc.markobot.dsl.*
            
            fun badContext() {
                робот("Wall-e") {
                    пластик толщиной 2
                }
            }
        """)
    }

    @Test
    fun testContext3() {
        assertCompilationFails("""
            import csc.markobot.api.*
            import csc.markobot.dsl.*
            
            fun badContext() {
                робот("Wall-e") {
                    глаза {
                        лампы {
                            количество = 2
                            яркость = 10
                        }
                    }
                }
            }
        """)
    }

    @Test
    fun testContext4() {
        assertCompilationFails("""
            import csc.markobot.api.*
            import csc.markobot.dsl.*
            
            fun badContext() {
                робот("Wall-e") {
                    шасси = колеса {
                        пластик толщиной 2
                        диаметр = 4
                        количество = 2
                    }
                }
            }
        """)
    }

    @Test
    fun testContext5() {
        assertCompilationFails("""
            import csc.markobot.api.*
            import csc.markobot.dsl.*
            
            fun badContext() {
                робот("Wall-e") {
                    шасси = ноги {
                        диаметр = 4
                        количество = 2
                    }
                }
            }
        """)
    }

    @Test
    fun testContext6() {
        assertCompilationFails("""
            import csc.markobot.api.*
            import csc.markobot.dsl.*
            
            fun badContext() {
                робот("Wall-e") {
                    туловище {
                        металл толщиной 1
                        гусеницы шириной 10
                    }
                }
            }
        """)
    }

    private fun assertCompilationFails(source: String) {
        val result = KotlinCompilation().apply {
            sources = listOf(SourceFile.kotlin("BadCode.kt", source))
            inheritClassPath = true
        }.compile()

        Assertions.assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.exitCode)
    }
}