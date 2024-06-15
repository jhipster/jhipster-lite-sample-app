module.exports = {
  '{src/**/,}*.{js,ts,tsx,vue}': ['eslint --fix'],
  '*.{md,json,yml,html,css,scss,java,xml,feature}': ['prettier --write'],
};
