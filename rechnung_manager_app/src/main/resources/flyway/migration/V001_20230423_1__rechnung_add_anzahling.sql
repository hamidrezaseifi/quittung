
ALTER TABLE IF EXISTS public.rechnung
    ADD COLUMN anzahlung real NOT NULL DEFAULT 0;
