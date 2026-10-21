# Arquitetura inicial
O projeto usa uma arquitetura em camadas simples:
- **model**: classes de domínio e enums que representam doadores, famílias, produtos, estoque, doações, inspeções, liberações e destinações.
- **services**: operações da ONG central.
- **repository**: camada reservada para consultas e persistência no MySQL.
- **sql**: script de criação do banco relacional.

## Fluxo previsto
1. A ONG registra doadores e doações.
2. A doação é inspecionada e recebe um estado de aptidão.
3. Produtos aptos compõem o estoque central.
4. As necessidades das famílias são registradas e priorizadas.
5. A destinação registra reserva, conclusão ou cancelamento.

Esta etapa estabelece a estrutura inicial. A integração com MySQL e as regras completas serão implementadas posteriormente.