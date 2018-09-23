#ifndef __BUFFER_H
#define __BUFFER_H



/* Definicao do tipo "buffer_t"
ATENCAO: 'buffer_t' e um apontador para uma estrutura do tipo 'buffer_s'
buffer_s e' privada e tem a definicao em buffer.c */
typedef struct buffer_s *buffer_t;


/* Retorna um novo buffer devidamente inicializado */
extern buffer_t initBuffer(void);




/* Adiciona o caracter 'c' ao buffer
 Retorna 'true' se o buffer ficou cheio, 'false' caso contrario */
extern int appendBuffer(buffer_t buffer, char c);


/* Esvazia o buffer
 Deve simplesmente escrever no terminal os caracteres contidos no buffer */
extern void flushBuffer(buffer_t buffer);



#endif
