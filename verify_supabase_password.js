const { createClient } = require('@supabase/supabase-js');

// Config from your provided URL
const SUPABASE_URL = 'https://kwhhkiidbdykzkahxbpg.supabase.co';
const SUPABASE_KEY = 'YOUR_SUPABASE_ANON_KEY'; // You need to paste your anon key here from Settings -> API

const supabase = createClient(SUPABASE_URL, SUPABASE_KEY);

async function verifyPassword() {
    const email = 'pataharsha@gmail.com'; // Using your email from .env
    const password = 'Killlmonger.09'; // Trying the first password

    console.log(`Checking password for ${email}...`);

    const { data, error } = await supabase.auth.signInWithPassword({
        email: email,
        password: password,
    });

    if (error) {
        console.log('❌ Password is wrong or user does not exist');
        console.log('Error details:', error.message);

        // Try the second password if the first failed
        console.log('\nTrying second password: Openlong@512...');
        const { data: data2, error: error2 } = await supabase.auth.signInWithPassword({
            email: email,
            password: 'Openlong@512',
        });

        if (error2) {
            console.log('❌ Second password also failed.');
        } else {
            console.log('✅ Success! The correct password is: Openlong@512');
        }
    } else {
        console.log('✅ Success! The correct password is: Killlmonger.09');
    }
}

console.log("NOTE: To run this script, you must replace 'YOUR_SUPABASE_ANON_KEY' with the key found in your Supabase Dashboard under Settings > API.");
// verifyPassword(); // Uncomment this line and run with 'node verify_supabase_password.js'
