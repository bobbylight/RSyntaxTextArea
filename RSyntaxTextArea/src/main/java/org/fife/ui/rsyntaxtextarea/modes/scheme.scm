(if #t "yes" "no")

(<= 3 8/3)

(define x #xEEFF88)
(define y #o53)
(define rational 10/15)
(define integer 19191)
(define complex -54+3i)

(define (make-window . args)
  (let ((depth  (get-keyword-value args #:depth  screen-depth))
        (bg     (get-keyword-value args #:bg     "white"))
        (width  (get-keyword-value args #:width  800))
        (height (get-keyword-value args #:height 100))
        ...)
    ...))

" this is a
multiline string"

(rationalize (inexact->exact 1.2) 1/100)

(lambda (a b c d e) '())
(lambda* (a b #:optional c d . e) '())