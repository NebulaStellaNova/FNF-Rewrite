# Friday Night Funkin': Rewrite Guidelines for Claude

This project is a rewritten version of the popular rhythm game **Friday Night Funkin'**. This
codebase has strict guidelines for how things should be done. This project is built using FlixelGDX, a Java-based
translation of the HaxeFlixel framework.

---

## Collaboration before implementation

Treat the interaction as teamwork, not robotic task execution. Prefer brainstorming when the user's direction is ambiguous.

Before implementing anything (planning or coding):

1. Ask yourself whether the requested design or refactor is actually good for FlixelGDX.
2. If it hurts the framework, breaks invariants, or there is clearly a better path, **stop before editing files or running commands**.
3. Explain why (pros and cons), suggest better alternatives, and ask whether the user still wants to proceed.
4. If they confirm after that discussion, proceed as requested.

---

## Explaining things for beginners and contributors

The framework welcomes new contributors learning open source.

When explaining code or introducing patterns:

- Explain **why** before **how** (motivation before mechanics).
- Use analogies for complex systems; if the user gave no analogy topic, ask for one they like.
- End **complex** explanations with a short check-in question so you can verify understanding.
- Stay encouraging and professional. Assume intelligence but not deep familiarity with Java, libGDX, or FlixelGDX quirks.

---

## Code quality (non-negotiables)

### Performance, Memory, and allocations

- **Do not allocate objects inside loops or in methods invoked every frame.** That rule is strict. Prefer reuse, pooling (FlixelGDX and libGDX),
  indexed `for` loops, and performance-oriented helpers such as FlixelGDX's `FlixelString` or libGDX's `ObjectMap`.
- **Always put fields in the correct order for each class**. This one is also an extremely important rule to follow, and it's
  vital you **do not forget this**. This rule is specifically for ensuring objects are as lean and small as possible and to keep
  alignment padding as tight as possible. Follow the order below:

    1. `long`s and `double`s
    2. `int`s, `float`s, and object references
    3. `short`s and `char`s
    4. `boolean`s and `byte`s

### Coding style

**Always put fields, modifiers, types, and methods in the correct order**. This keeps the code readable and consistent.
Follow the orders below:

#### Modifiers

1. `public`
2. `protected`
3. default
4. `private`
5. `static`
6. `final`

#### Fields, Methods/Functions and Types

1. Fields (following the alignment padding rule!)
2. Constructors, with smallest to largest parameters top to bottom
3. Methods (if there are overloads, order them smallest to largest parameters top to bottom)
4. Simple Getter/Setter methods (below every other method)
5. Inner classes
6. Inner interfaces
7. Inner enums

#### Example

```kotlin
package org.flixelgdx.example

/**
 * Rich and detailed KDoc here.
 *
 * Other details here.
 */
open class PerformanceObject(
    var instanceId: Int = -1,
    var displayName: String? = "GENERIC_INSTANCE",
    protected var velocity: Float = 0.0f,
    var isActive: Boolean = false
) {
    companion object {
        const val GLOBAL_LIMIT: Long = 5000L
        const val MAX_RETRIES: Int = 5

        var systemNodeName: String = "PRIMARY_NODE"

        @JvmField
        var systemMode: Char = 'V'

        @JvmField
        protected var internalWeight: Double = 42.42

        const val VERSION_STABILITY: Double = 1.0
        const val GRAVITY_CONSTANT: Float = 9.81f

        private val sharedLock = Any()
        private var systemHeader: Byte = 0x1

        fun sayHello() {
            println("Hello, world!")
        }
    }

    val createdAt: Long = System.currentTimeMillis()

    private var sensorValue: Double = 0.0
    private var metadata: List<String>? = null

    var localFlags: Short = 0
    private var categoryCode: Char = '\u0000'
    private var checksum: Byte = 0

    fun logMessage(msg: String = "Default system heartbeat check.", level: Int = 1) {
        println("[$level] $msg")
    }

    protected fun processData() {
        this.checksum = 0x00
    }

    private fun addNum(f1: Float, f2: Float): Float {
        return f1 + f2
    }

    private class InternalConfig {
        private val timeout: Long = 0
        private val bufferSize: Int = 0
    }

    interface StateListener {
        fun onTransition(success: Boolean)
    }

    enum class Priority {
        LOW,
        MEDIUM,
        HIGH
    }
}
```

### Architecture and scope

- `core` is the module where the game's code lives. Keep backend logic out of the core logic. If you must do something that's
  platform specific, put it in another backend module like `lwjgl3`.

### Language and style

- Target **Java 17**. Prefer modern features Kotlin features (like `?.` operators over `!!`, class-header constructors over `constructor() {}`, etc).
- **Import** every type you use. Do **not** use star imports (`*`).
- Do **not** use fully qualified class names inline when a normal import would read cleanly.
- Empty or void-returning methods: opening brace on the same line per `.editorconfig` (example: `fun hook() {}`).
- Prefer **short** field and method names. If shortening a method name hides its meaning, use a concise name plus KDoc instead of a long identifier.

---

## Documentation, comments, and KDoc

Documentation should read like a **beginner-friendly handbook**, not an expert-only manual.

### Mechanics

- Use correct grammar and punctuation everywhere (comments, `@param`, `@return`, `@throws`, and so on).
- Stick to **ASCII** in prose when practical; avoid decorative punctuation like en dash, em dash, fancy arrows, or emojis. This applies
  to both source comments and KDoc. Use a plain hyphen (-) only for compound adjectives; never use it as a sentence separator or
  stand-in for an em dash. This rule also applies to inline comments in Groovy/Gradle files. This allows the docs to be read easily 
  on every device and requires you to use clarity over brevity. The Markdown docs are the only exception to this rule.
- Use **consistent capitalization and grammar** in prose and code.
- Every doc comment should always start with a single sentence, with detailed paragraphs following.
- Include the right KDoc tags (`@param`, `@return`, `@throws`, …) wherever they apply.
- Use nullability annotations (`@Nullable`, `@NotNull`) where they help tooling.
- Keep link (`[SomeClassHere]`) references valid or fix broken links.
- Follow `.editorconfig` for formatting.
- Add comments where either complexity would otherwise be hard to follow, or where code requires import context.
- Skip KDoc on trivial, self-explanatory methods (plain getters/setters or something like `calculateTotal()` unless there is subtle behavior).
- Prefer **American English** in docs. (e.g., "behavior" instead of "behaviour")
- After code changes that affect public behavior or APIs, **update relevant Markdown docs** in the repo.
- Don't use section comments (like `// ---`). The code should be easily navigable simply by how it's organized; section comments are just noise.

### Comments versus KDocs

- **In line comments**, do **not** use Markdown tricks (bold with `**`, backticks around snippets, etc.). Reserve richer formatting for KDoc.
- When naming methods inside comments, include parentheses (`someMethod()`). If parameters exist, but you are not spelling them out, use `anotherMethod(...)`. Example class-qualified form: `SomeClass.someMethod(...)`.
- **In comments**, prefer `SomeClass.someMethod(...)` instead of KDoc-style `SomeClass#someMethod(int)`.

### Heavily used or critical APIs

For widely used classes, fields, methods, or anything central to correctness, include a **small usage example** in KDoc showing correct typical use.

---

## When research is needed

If the task needs external reference beyond this repo:

1. Start from these canonical sources:

    - `https://flixelgdx.org/api/`
    - `https://libgdx.com/wiki/`
    - `https://github.com/flixelgdx/flixelgdx`
    - `https://github.com/libgdx/libgdx`

2. If those are not enough or off-topic for the question, broaden the search thoughtfully.
