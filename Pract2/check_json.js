function isValidJSON(text) {
    try {
        JSON.parse(text);
        return true;
    } catch (e) {
        return false;
    }
}

const testCases = [
    '{"name": "Деркач", "age": 20}',
    '[1, 2, 3]',
    '{invalid json}',
    '"просто рядок"',
    'не json взагалі',
];

testCases.forEach(text => {
    const result = isValidJSON(text) ? 'Дійсний JSON' : 'Недійсний JSON';
    console.log(`${result}: ${text}`);
});
