import sqlite3

def export_to_sql():
    conn = sqlite3.connect('clinical_data.db')
    cursor = conn.cursor()
    tables = ['users', 'patients', 'scans', 'clinical_findings', 'clinical_notes', 'otps', 'audit_logs']
    
    output_file = 'docs/LOCAL_DATA_EXPORT.sql'
    
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write("-- Oral Surgery AI - Local Data Export for Supabase\n")
        f.write("-- Generated from clinical_data.db\n\n")
        f.write("BEGIN;\n\n")

        for table in tables:
            try:
                cursor.execute(f"SELECT * FROM {table}")
                rows = cursor.fetchall()
                if not rows:
                    continue
                
                cols = [description[0] for description in cursor.description]
                cols_str = ", ".join(cols)
                
                f.write(f"-- Data for table: {table}\n")
                for row in rows:
                    vals = []
                    for v in row:
                        if v is None:
                            vals.append("NULL")
                        elif isinstance(v, str):
                            # Escape single quotes for SQL
                            escaped_v = v.replace("'", "''")
                            vals.append(f"'{escaped_v}'")
                        else:
                            vals.append(str(v))
                    
                    vals_str = ", ".join(vals)
                    f.write(f"INSERT INTO {table} ({cols_str}) VALUES ({vals_str}) ON CONFLICT DO NOTHING;\n")
                f.write("\n")
            except Exception as e:
                f.write(f"-- Error exporting {table}: {str(e)}\n")

        f.write("COMMIT;\n")
    
    print(f"✅ Data exported successfully to {output_file}")

if __name__ == "__main__":
    export_to_sql()
