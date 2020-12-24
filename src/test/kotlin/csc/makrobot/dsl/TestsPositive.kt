@file:Suppress("LocalVariableName", "NonAsciiCharacters")

package csc.makrobot.dsl

import csc.markobot.api.*
import csc.markobot.dsl.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestsPositive {

    @Test
    fun testNonDSL() {
        val робот = MakroBot("Wall-E",
            Head(Plastik(2), listOf(LampEye(10), LampEye(10), LedEye(3)), Mouth(Speaker(3))),
            Body(Metal(1), listOf("I don't want to survive.", "I want live.")),
            Hands(Plastik(3), LoadClass.Light, LoadClass.Medium),
            Chassis.Caterpillar(10)
        )
        verify(робот)
    }

    @Test
    fun testDSL() {
        val робот = робот("Wall-E") {
            голова {
                пластик толщиной 2

                глаза {
                    лампы {
                        количество = 2
                        яркость = 10
                    }
                    диоды {
                        количество = 1
                        яркость = 3
                    }
                }

                рот {
                    динамик {
                        мощность = 3
                    }
                }
            }

            туловище {
                металл толщиной 1

                надпись {
                    +"I don't want to survive."
                    +"I want live."
                }
            }

            руки {
                пластик толщиной 3
                нагрузка = легкая - средняя
            }

            шасси = гусеницы шириной 10
        }

        verify(робот)
    }

    @Test
    fun testDSLOtherChassis() {
        val роботНаКолесах = робот("Wall-E") {
            голова {
                пластик толщиной 2

                глаза {
                    лампы {
                        количество = 2
                        яркость = 10
                    }
                }

                рот {
                    динамик {
                        мощность = 3
                    }
                }
            }

            туловище {
                металл толщиной 1
            }

            руки {
                пластик толщиной 3
                нагрузка = легкая - средняя
            }
            шасси = колеса {
                диаметр = 4
                количество = 2
            }
        }

        Assertions.assertEquals(Chassis.Wheel(2, 4), роботНаКолесах.chassis)

        val роботНаНогах = робот("Wall-E") {
            голова {
                пластик толщиной 2

                глаза {
                    лампы {
                        количество = 2
                        яркость = 10
                    }
                }

                рот {
                    динамик {
                        мощность = 3
                    }
                }
            }

            туловище {
                металл толщиной 1
            }

            руки {
                пластик толщиной 3
                нагрузка = легкая - средняя
            }
            шасси = ноги
        }

        Assertions.assertEquals(Chassis.Legs, роботНаНогах.chassis)
    }

    private fun verify(робот: MakroBot) {
        Assertions.assertEquals("Wall-E", робот.name)
        Assertions.assertEquals(Plastik(2), робот.head.material)
        Assertions.assertArrayEquals(arrayOf(LampEye(10), LampEye(10), LedEye(3)), робот.head.eyes.toTypedArray())
        Assertions.assertEquals(Mouth(Speaker(3)), робот.head.mouth)

        Assertions.assertEquals(Metal(1), робот.body.material)
        Assertions.assertArrayEquals(arrayOf("I don't want to survive.", "I want live."), робот.body.strings.toTypedArray())

        Assertions.assertEquals(Plastik(3), робот.hands.material)
        Assertions.assertEquals(LoadClass.Light, робот.hands.minLoad)
        Assertions.assertEquals(LoadClass.Medium, робот.hands.maxLoad)

        Assertions.assertEquals(Chassis.Caterpillar(10), робот.chassis)
    }
}
