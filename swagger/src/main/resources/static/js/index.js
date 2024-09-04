console.log(services);

// endpoint body element colapse/expand
document.querySelectorAll('header.header').forEach(header => header.addEventListener('click', function() {
    this.classList.toggle('active');
}));



// generating endpoint object body
document.querySelectorAll('.model-example').forEach(element => {
    const elementDataModelAttribute = element.getAttribute('data-model');
    try {
        const jsonData = JSON.parse(elementDataModelAttribute);

        for (const key in jsonData) {
            if (jsonData[key] === 'localdatetime') {
                jsonData[key] = '<span style=\'color: #a2fca2\'>' + new Date().toISOString().replace('T', ' ').replace('Z', '') + '</span>';
            } else if (jsonData[key] === 'boolean') {
                jsonData[key] = '<span style=\'color: #fcc28c\'>true</span>';
            }
        }

        element.innerHTML = '<pre><code>' + JSON.stringify(jsonData, undefined, 2) + '</code></pre>';
    } catch (error) {
        element.innerHTML = '<pre><code>' + JSON.stringify(elementDataModelAttribute, undefined, 2) + '</code></pre>';
    }
});