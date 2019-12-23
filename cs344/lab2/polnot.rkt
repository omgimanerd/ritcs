; The Polnot language interpreter
; 
; Alvin Lin (axl1439)

(define OPERATORS '(+ - * /))

; Gets a symbol definition from an environment, #f if it is not defined.
; Environment format: ((a 3) (d 2) .. )
(define env-get (lambda (env symbol)
  (if (null? env)
    #f
    (if (eq? symbol (caar env))
      (cadar env)
      (env-get (cdr env) symbol)
    )
  )
))

; Sets a symbol to a value to in a given environment and returns the new
; environment.
(define env-set (lambda (env symbol value)
  (if (null? env)
    (list (list symbol value))
    (if (eq? symbol (caar env))
      (cons (list symbol value) (cdr env))
      (cons (car env) (env-set (cdr env) symbol value))
    )
  )
))

; Helper function for executing a mathematical operation
(define apply-operator (lambda (operation operand1 operand2)
  (cond
    ((eq? operation '+) (+ operand1 operand2))
    ((eq? operation '-) (- operand1 operand2))
    ((eq? operation '*) (* operand1 operand2))
    ((eq? operation '/) (/ operand1 operand2))
  )
))

; Here is a sample program to test the interpreter.
(define prog1
  '(
    (:= a 5)
    (:= b + + a 2 * a 10)
    (:= c / - b 5 4)
    (print a)
    (print b)
    (print c)
    (:= a + 660 - c 3)
    (print a)
    (print b)
    (print c)
    (print + a + b c)
    (print + 1 d)
    (print + 1 & 5 6)
  )
)

; run is the one of two public functions. There is a single parameter,
; a Polnot program instance. run starts with an empty environment and
; interprets every statement in the program, from first to last.

; A Polnot program is a list of sublists. Each sublist represents a
; statement.
;
; There are only two statements in Polnot.
;  assignment: a list starting with := and a symbol (other than an operator),
;    and followed by an expression.
;      The value of the expression is assigned to the variable named by
;      the symbol.
;  print: a list starting with print and followed by an expression.
;      The value of the expression is displayed on a separate line.
;
; Details on what consititues an expression are in the documentation for
; the evaluate function, below.
;
; The behavior of this function with any other argument structure is
; undefined.
;
(define run (lambda (pnprog)
  (letrec
    (
      (environment '())
      (run-r (lambda (prog env)
        (cond
          (
            (null? prog)
            env
          )
          (
            (eq? (caar prog) ':=)
            (run-r (cdr prog) (env-set env (cadar prog) (evaluate (cddar prog) env)))
          )
          (
            (eq? (caar prog) 'print)
            (display (evaluate (cdar prog) env))
            (newline)
            (run-r (cdr prog) env)
          )
        )
      ))
    )
    (run-r pnprog environment)
  )
))

; The other public function in the Polnot package is evaluate.
;
; Evaluate a prefix expression, stored in the list that is the first
; argument, using the environment stored in the second argument.
; Return the answer computed.
;
; The contents of the first argument list can be
; 1. a number
; 2. an operator symbol, followed by two sequences of tokens that on their
;    own would qualify as legal contents of the list argument to this
;    function. Legal operators are those for the four basic arithmetic
;    operations '+, '-, '*, and '/ .
; 3. a symbol, which is evaluated via a lookup in the environment argument
;
; In other words, the first argument would be the same as a Polnot assignment
; statement with the := and identifier tokens removed, or a Polnot print
; statement with the print token removed.
;
; If the expression is illegal due to insufficient tokens, the behavior
; of this funtion is undefined.
; If the the expression is illegal is illegal due to extra tokens, a valid
; result based on the tokens consumed is returned.
; If an unknown symbol is encountered, the value 999 is returned.
; If an unknown operator is encountered, it is treated as an unknown
; variable with extra tokens in the statement.
(define evaluate (lambda (expr env)
  (letrec
    (
      (evaluate-r (lambda (expr env)
        (letrec
          (
            (token (car expr))
            (rest (cdr expr))
          )
          (if (symbol? token)
            (if (member token OPERATORS)
              (let*
                (
                  (operand1 (evaluate-r rest env))
                  (operand2 (evaluate-r (cdr operand1) env))
                )
                (cons (apply-operator token (car operand1) (car operand2)) (cdr operand2))
              )
              (if (env-get env token)
                (cons (env-get env token) rest)
                (cons 999 rest)
              )
            )
            expr
          )
        )
      ))
    )
    (car (evaluate-r expr env))
  )
))

; Some suggested test expressions
(define final-env (run prog1))
(display final-env)
(newline)
(evaluate '(a) final-env)
(evaluate '(- - a b c) final-env)
(evaluate '(77) final-env)
(evaluate '(&) final-env)
(evaluate '(& 1 2 3 4 5 a b c) final-env)
(define empty-env '())
(evaluate '(a) empty-env)
(evaluate '(77) empty-env)
(evaluate '(&) empty-env)

