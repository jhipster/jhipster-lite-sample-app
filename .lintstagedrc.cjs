module.exports = {
  '{src/**/,}*.{ts,vue}': ['eslint --fix'],
  '*.{md,json,yml,html,css,scss,java,xml,feature}': ['prettier --write'],
};
