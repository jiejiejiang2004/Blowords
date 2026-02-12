// eslint.config.js
import eslintPluginVue from 'eslint-plugin-vue'
import tsParser from '@typescript-eslint/parser'
import tsPlugin from '@typescript-eslint/eslint-plugin'
import vueParser from 'vue-eslint-parser'

export default [
    {
        files: ['src/**/*.{vue,ts}'],
        ignores: ['node_modules/', 'dist/'],
        languageOptions: {
            parser: vueParser,
            parserOptions: {
                parser: tsParser,
                ecmaVersion: 'latest',
                sourceType: 'module',
                project: './tsconfig.app.json',
                extraFileExtensions: ['.vue']
            },
            globals: { browser: true, node: true }
        },
        plugins: {
            vue: eslintPluginVue,
            '@typescript-eslint': tsPlugin
        },
        rules: {
            ...eslintPluginVue.configs['flat/recommended'].rules,
            ...tsPlugin.configs.recommended.rules,
            'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'warn',
            'vue/multi-word-component-names': 'off',
            '@typescript-eslint/no-explicit-any': 'warn',
            "@typescript-eslint/no-unused-vars": "off"
        }
    }
]