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

## Regras da Doação
1-Princípios Fundamentais: A ajuda deve ser baseada estritamente na necessidade (imparcialidade), preservar a dignidade dos assistidos e manter total transparência.

2-Critérios de Vulnerabilidade (Pontuação): O sistema deve priorizar quem tem maior urgência com base em:

  .Composição familiar: Gestantes, lactantes, crianças pequenas, idosos e pessoas com deficiência.

  .Tempo de chegada: Recém-chegados sem rede de apoio têm prioridade máxima.

   .Situação de moradia: Pessoas em abrigos ou rua versus quem já tem habitação fixa.

3-Controle de Fluxo e Cotas:

   .Janelas de tempo (Cooldown): Intervalos mínimos entre retiradas (ex: 30 dias para cestas básicas) para evitar acúmulo.

   .Cotas por família: A quantidade de itens varia de acordo com o número de dependentes cadastrados.

   .Histórico rigoroso: Registro de quem retirou, o que foi entregue e a data para evitar duplicidades e fraudes.

4-Logística Operacional: Agendamento de horários para evitar filas longas e separação de uma porcentagem do estoque para reservas de emergência.
