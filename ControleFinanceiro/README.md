# Controle Financeiro (Android + Kotlin)

Aplicativo nativo Android para controle financeiro simples, permitindo registrar
lançamentos (Receitas/Despesas) e visualizá-los em uma lista com o saldo total.

## Como abrir o projeto

1. Abra o **Android Studio** (Hedgehog ou mais recente recomendado).
2. Escolha **Open** e selecione a pasta `ControleFinanceiro` (a pasta raiz deste projeto).
3. Aguarde o Gradle sincronizar (ele vai baixar as dependências automaticamente).
4. Rode em um emulador ou dispositivo físico (botão "Run" ▶️, `minSdk 24` / Android 7.0+).

## Arquitetura: MVVM

O projeto segue o padrão **MVVM (Model-View-ViewModel)**, organizado em pacotes:

```
com.example.controlefinanceiro
├── model/          -> Model: classes de dados (Lancamento, TipoLancamento)
├── data/            -> Persistência: Room (AppDatabase, DAO, Repository, Converters)
├── viewmodel/       -> ViewModel: lógica de apresentação (LancamentoViewModel + Factory)
├── view/            -> View (auxiliar): Adapter do RecyclerView
├── MainActivity.kt    -> View: Tela de Lançamento (Cadastro)
└── ExtratoActivity.kt -> View: Tela de Extrato (Fluxo de Caixa)
```

**Fluxo de dados:**

`Activity (View)` → chama → `ViewModel` → chama → `Repository` → chama → `DAO (Room)` → SQLite

A lista de lançamentos usa **LiveData**: sempre que um novo lançamento é salvo no
banco, a tela de Extrato é atualizada automaticamente, sem recarregar manualmente.

## Persistência de dados

Os dados são salvos localmente usando **Room** (abstração sobre SQLite). O banco é
criado automaticamente na primeira execução do app, e os dados continuam salvos
mesmo depois de fechar o aplicativo.

## Funcionalidades implementadas (checklist do enunciado)

- [x] Tela principal (Lançamento/Cadastro)
- [x] Tela de listagem (Extrato)
- [x] Validação dos campos de entrada (valor, descrição, data)
- [x] Persistência de dados (Room/SQLite)
- [x] Navegação entre telas (Intent)
- [x] Organização em camadas MVVM
- [x] Lista com RecyclerView + Adapter
- [x] Uso de DatePicker (bônus)
- [x] Diferenciação visual Receita/Despesa por cor e ícone (bônus)
- [x] Exibição do saldo total (bônus)

Todo o código está comentado em português, explicando o papel de cada
classe/função dentro da arquitetura.
