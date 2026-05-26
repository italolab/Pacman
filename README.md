# Jogo de Pacman

Veja abaixo uma captura de tela do jogo de Pacman:

!['Jogo de Pacman em Java'](pacman.png)

## Detalhes técnicos

Esse jogo foi produzido em Java e depende do JRE8 ou superior para funcionar. Os gráficos foram feitos com uso de primitivas gráficas. Logo, os gráficos do jogo são conjuntos de polígonos, linhas, retângulos e círculos. Os personagens e outros objetos do jogo não foram desenhados com pixelart!

Os arquivos de áudio estão em formato MP3 e estão embutidos no jar da aplicação, junto com a biblioteca de audio JLayer para java.

Esta foi uma ótima oportunidade para aplicar alguns padrões de projeto e separar a lógica da aplicação dos gráficos e interface gráfica.

## A inteligência artificial dos monstrinhos

Foi implantado para o movimento dos personagens monstrinhos o A*: um algoritmo de inteligência artificial simbólica que é uma variação do algorítmo do caminho mínimo de Dijkstra que pode ser aplicado em jogos com obstáculos para determinar o caminho mais curto de uma origem até um destino.

Graças ao A*, os monstrinhos chegam tão fácil até onde está o pacman, aumentando o nível de dificuldade do jogo.

## Como executar?

Para rodar o jogo, basta baixar o arquivo Pacman-5.0.jar e rodar executando o seguinte comando:

```
java -jar Pacman-5.0.jar
```

Obs, para funcionar depende do java 1.8 ou superior instalado na máquina

## Como jogar?

Para jogar, utilize as setas do teclado para mover para cima/baixo/traz/frente no tabuleiro e utilize o enter para pausar. O jogo suporta os seguintes truques:

> É aconselhável pausar o jogo com um enter para então digitar os truques!

* <u><b>Pular de fase</b></u>: o jogo tem cinco fases ao todo. Para pular de fase, basta digitar o conjunto de teclas: fase(N). Onde, (N) é o número da fase. Ex: "fase3", pula para fase 3. "fase5" vai para fase 5 (última fase).

* <u><b>Aumentar número de vidas</b></u>: para aumentar o número de vidas, digite: vidas(NNN), onde (NNN) é um número de 3 digitos correspondende ao número de vidas que você desejar colocar. Ex: "vidas100" coloca 100 vidas. "vidas999" coloca 999 vidas. vidas001 coloca apenas 1 vida.

* <u><b>Atravessar paredes</b></u>: para atravessar as paredes com o pacman, basta digitar as teclas: ap. Ex: digite "ap" para atravessar paredes e, caso queira retirar o truque de atravessar paredes, basta digitar "ap" novamente.

> Digite os truques sem as aspas e sem parênteses