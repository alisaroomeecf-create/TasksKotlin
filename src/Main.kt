
    data class Account(
        val cardNumber: String,
        var pinCode: String,
        var balance: Double
    )

    object BankDatabase {
        private val accounts = mutableMapOf(
            "1111-2222" to Account("1111-2222", "1234", 5000.0),
            "3333-4444" to Account("3333-4444", "4321", 15000.50),
            "5555-6666" to Account("5555-6666", "0000", 100.0)
        )

        fun getAccount(cardNumber: String): Account? = accounts[cardNumber]
    }

    class ATM {

        fun start() {
            println("   Добро пожаловать в банкомат!  ")
            var currentAccount: Account? = null
            var attempts = 3
            while (currentAccount == null && attempts > 0) {
                print("Вставьте карту (номер карты): ")
                val cardNumber = readln().trim()
                val account = BankDatabase.getAccount(cardNumber)

                when (account) {
                    null -> println("Карта не распознана. Попробуйте снова.\n")
                    else -> {
                        print("Введите ПИН-код: ")
                        val pin = readln().trim()

                        when (pin) {
                            account.pinCode -> {
                                currentAccount = account
                                println("\n Авторизация успешна. Здравствуйте!")
                            }

                            else -> {
                                attempts--
                                println("Неверный ПИН-код. Осталось попыток: $attempts\n")
                                when (attempts) {
                                    0 -> {
                                        println("Карта заблокирована. Заберите карту.")
                                        return
                                    }
                                }
                            }
                        }
                    }
                }
            }

            var isRunning = true
            while (isRunning) {
                println("\n Главное меню ")
                println("1. Проверить баланс")
                println("2. Снять наличные")
                println("3. Пополнить счет")
                println("4. Завершить работу")
                print("Выберите действие: ")

                when (readln().trim()) {
                    "1" -> currentAccount?.let { checkBalance(it) }
                    "2" -> currentAccount?.let { withdrawMoney(it) }
                    "3" -> currentAccount?.let { depositMoney(it) }
                    "4" -> {
                        println("Завершение работы. Заберите карту.")
                        isRunning = false
                    }

                    else -> println("Неверный пункт меню. Попробуйте снова.")
                }
            }
        }

        private fun checkBalance(account: Account) {
            println("Ваш текущий баланс: ${String.format("%.2f", account.balance)} руб.")
        }

        private fun withdrawMoney(account: Account) {
            print("Введите сумму для снятия (минимум 100): ")
            val amount = readln().trim().toDoubleOrNull()
            when {
                amount == null || amount <= 0 -> {
                    println(" Некорректная сумма. Введите число больше нуля.")
                }

                amount % 100 != 0.0 -> {
                    println("Банкомат выдает купюры только номиналом 100, 500, 1000, 5000. Сумма должна быть минимум 100.")
                }

                amount > account.balance -> {
                    println(" Недостаточно средств на счете.")
                }

                else -> {
                    account.balance -= amount
                    println("Успешно снято: ${String.format("%.2f", amount)} руб.")
                    println("Новый баланс: ${String.format("%.2f", account.balance)} руб.")
                }
            }
}

            private fun depositMoney(account: Account) {
                print("Внесите купюры в купюроприемник. Введите сумму: ")
                val amount = readln().trim().toDoubleOrNull()
                when {
                    amount == null || amount <= 0 -> {
                        println(" Некорректная сумма. Введите число больше нуля.")
                    }
                    else -> {
                        account.balance += amount
                        println("Счет пополнен на: ${String.format("%.2f", amount)} руб.")
                        println("Новый баланс: ${String.format("%.2f", account.balance)} руб.")
                    }
                }
            }
     }
    fun main(){
        ATM().start()
    }