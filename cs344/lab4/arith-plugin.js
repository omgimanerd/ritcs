/**
 * @fileoverview CSCI 344: Programming Language Concepts Lab 4
 * @author Alvin Lin (axl1439)
 */

function Arith() {
  this.stack = [0]
}

Arith.prototype.enter = function() {
  this.stack.push(this.getStackTop())
}

Arith.prototype.getStackTop = function() {
  return this.stack.length ? this.stack[this.stack.length - 1] : 0
}

Arith.prototype.setStackTop = function(v) {
  this.stack[this.stack.length - 1] = v
}

Arith.prototype.appendNumber = function(n) {
  this.setStackTop(this.getStackTop() * 10 + n)
}

Arith.prototype.pop = function() {
  return this.stack.length ? this.stack.pop() : 0
}

Arith.prototype.add = function() {
  this.stack.push(this.pop() + this.pop())
}

Arith.prototype.subtract = function() {
  this.stack.push(-this.pop() + this.pop())
}

Arith.prototype.multiply = function() {
  this.stack.push(this.pop() * this.pop())
}

Arith.prototype.divide = function() {
  var divisor = this.pop()
  var dividend = this.pop()
  this.stack.push(dividend / divisor)
}

Arith.opList = {
  '+': Arith.prototype.add,
  '-': Arith.prototype.subtract,
  '*': Arith.prototype.multiply,
  '/': Arith.prototype.divide
}

Arith.opNames = ['+', '-', '*', '/']
