// Exécuté automatiquement au 1er démarrage si le volume Mongo
db = db.getSiblingDB('notesdb');

// Utilisateur applicatif
if (!db.getUser('note_user')) {
  db.createUser({
    user: 'note_user',
    pwd: 'note_pass',
    roles: [{ role: 'readWrite', db: 'notesdb' }]
  });
}

// Collection + index
if (!db.getCollectionNames().includes('notes')) {
  db.createCollection('notes');
}
db.notes.createIndex({ patientId: 1 }, { background: true });

