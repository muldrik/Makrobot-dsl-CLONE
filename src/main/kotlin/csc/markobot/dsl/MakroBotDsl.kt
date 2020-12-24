@file:Suppress("EnumEntryName", "NonAsciiCharacters")

package csc.markobot.dsl

import csc.markobot.api.*

object пластик {
    var thickness = 0
}

object металл {
    var thickness = 0
}

object гусеницы {
    var width = 0
}

object колесо {
    var number = 0
    var diameter = 0
}

typealias ноги = Chassis.Legs

val оченьЛегкая = LoadClass.VeryLight
val легкая = LoadClass.Light
val средняя = LoadClass.Medium
val тяжелая = LoadClass.Heavy
val оченьТяжелая = LoadClass.VeryHeavy

@MakroBotDsl
class MakroBotBuilder {
    lateinit var имя: String
    lateinit var голова: Head
    lateinit var туловище: Body
    lateinit var руки: Hands
    lateinit var шасси: Chassis
    infix fun гусеницы.шириной(width: Int) = Chassis.Caterpillar(width)
}

fun MakroBotBuilder.голова(settings: HeadBuilder.()->Unit) {
    val голова = HeadBuilder().apply(settings)
    this.голова = Head(голова.материал, голова.глаза, голова.рот)
}

fun MakroBotBuilder.туловище(settings: BodyBuilder.()->Unit) {
    val туловище = BodyBuilder().apply(settings)
    this.туловище = Body(туловище.материал, туловище.надпись)
}

fun MakroBotBuilder.руки(settings: HandsBuilder.() -> Unit) {
    val руки = HandsBuilder().apply(settings)
    this.руки = Hands(руки.материал, руки.нагрузка.start, руки.нагрузка.endInclusive)
}

@MakroBotDsl
class HeadBuilder {
    lateinit var материал: Material
    var глаза = mutableListOf<Eye>()
    lateinit var рот: Mouth
    infix fun пластик.толщиной(thickness: Int) {
        материал = Plastik(thickness)
    }
    infix fun металл.толщиной(thickness: Int) {
        материал = Metal(thickness)
    }
}

fun HeadBuilder.глаза(settings: EyeBuilder.() -> Unit) {
    val глаза = EyeBuilder().apply(settings)
    this.глаза = глаза.глаза
}

fun HeadBuilder.рот(settings: MouthBuilder.() -> Unit) {
    val рот = MouthBuilder().apply(settings)
    this.рот = рот.рот
}

@MakroBotDsl
class BodyBuilder {
    var материал: Material = Metal(0)
    var надпись = mutableListOf<String>()
    infix fun металл.толщиной(thickness: Int) {
        материал = Metal(thickness)
    }
    infix fun пластик.толщиной(thickness: Int) {
        материал = Plastik(thickness)
    }
}

@MakroBotDsl
class StringsBuilder {
    val надпись = mutableListOf<String>()
    operator fun String.unaryPlus() {
        надпись.add(this)
    }
}

fun BodyBuilder.надпись(settings: StringsBuilder.() -> Unit) {
    val надпись = StringsBuilder().apply(settings)
    this.надпись.addAll(надпись.надпись)
}

@MakroBotDsl
class HandsBuilder {
    var материал: Material = Metal(0)
    var нагрузка = оченьЛегкая..оченьЛегкая
    infix fun металл.толщиной(thickness: Int) {
        материал = Metal(thickness)
    }
    infix fun пластик.толщиной(thickness: Int) {
        материал = Plastik(thickness)
    }
    operator fun LoadClass.minus(максНагрузка: LoadClass) = this..максНагрузка
}

@MakroBotDsl
class WheelBuilder {
    var количество = 0
    var диаметр = 0
}


fun колеса(settings: WheelBuilder.() -> Unit): Chassis.Wheel {
    val колесо = WheelBuilder().apply(settings)
    return Chassis.Wheel(колесо.количество, колесо.диаметр)
}

@MakroBotDsl
class EyeBuilder {
    val глаза = mutableListOf<Eye>()
}

@MakroBotDsl
class LedEyeBuilder {
    var количество = 0
    var яркость = 0
}

@MakroBotDsl
class LampEyeBuilder {
    var количество = 0
    var яркость = 0
}

fun EyeBuilder.диоды(setting: LedEyeBuilder.()->Unit) {
    val диоды = LedEyeBuilder().apply(setting)
    this.глаза.addAll(List(диоды.количество) { LedEye(диоды.яркость) })
}

fun EyeBuilder.лампы(settings: LampEyeBuilder.() -> Unit) {
    val лампы = LampEyeBuilder().apply(settings)
    this.глаза.addAll(List(лампы.количество) { LampEye(лампы.яркость) })
}

@MakroBotDsl
class MouthBuilder {
    lateinit var рот: Mouth
}

fun MouthBuilder.динамик(settings: SpeakerBuilder.() -> Unit) {
    val динамик = SpeakerBuilder().apply(settings)
    this.рот = Mouth(Speaker(динамик.мощность))
}

@MakroBotDsl
class SpeakerBuilder {
    var мощность = 0
}

fun робот(name: String, settings: MakroBotBuilder.() -> Unit): MakroBot {
    val markoBot = MakroBotBuilder().apply(settings)
    return MakroBot(name, markoBot.голова, markoBot.туловище, markoBot.руки, markoBot.шасси)
}