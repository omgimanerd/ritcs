/**
 * @fileoverview This module handles text formatting for tables.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

// eslint-disable-next-line no-unused-vars
const colors = require('colors')

exports.formatLeftColumnHeader = table => {
  return table.map(row => {
    row[0] = row[0].bold.red
    return row
  })
}

exports.formatReliability = reliability => {
  reliability = String(reliability)
  switch (reliability) {
  case 'great':
    return reliability.green
  case 'fair':
    return reliability.yellow
  case 'poor':
    return reliability.red
  }
  return reliability
}
