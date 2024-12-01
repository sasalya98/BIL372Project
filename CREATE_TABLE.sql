CREATE VIRTUAL TABLE SOLDIER_FTS USING fts5(
    id UNINDEXED,
    name,
    surname,
    soldier_rank,
    content='SOLDIER',
    content_rowid='id',
    tokenize="unicode61 tokenchars '-'"
);

CREATE VIRTUAL TABLE SOLDIER_FAMILY_FTS USING fts5(
    id UNINDEXED,
    name,
    surname,
    relation_type,
		phone_number,
    content='SOLDIER_FAMILY',
    content_rowid='id',
    tokenize="unicode61 tokenchars '-'"
);

CREATE VIRTUAL TABLE BASE_FTS USING fts5(
    id UNINDEXED,
    base_name,
    content='BASE',
    content_rowid='id',
    tokenize="unicode61 tokenchars '-'"
);

CREATE VIRTUAL TABLE OPERATION_FTS USING fts5(
    id UNINDEXED,
    operation_name,
    operation_type,
    content='OPERATION',
    content_rowid='id',
    tokenize="unicode61 tokenchars '-'"
);

CREATE VIRTUAL TABLE VEHICLE_FTS USING fts5(
    id UNINDEXED,
    vehicle_type,
    model,
    license_plate,
    content='VEHICLE',
    content_rowid='id',
    tokenize="unicode61 tokenchars '-'"
);

CREATE VIRTUAL TABLE DIVISION_FTS USING fts5(
    id UNINDEXED,
    division_name,
    content='DIVISION',
    content_rowid='id',
    tokenize="unicode61 tokenchars '-'"
);

CREATE VIRTUAL TABLE MISSION_FTS USING fts5(
    id UNINDEXED,
    mission_name,
    mission_type,
    content='MISSION',
    content_rowid='id',
    tokenize="unicode61 tokenchars '-'"
);

CREATE VIRTUAL TABLE MILITARY_EVENT_FTS USING fts5(
    id UNINDEXED,
    event_title,
    event_description,
    content='MILITARY_EVENT',
    content_rowid='id',
    tokenize="unicode61 tokenchars '-'"
);

CREATE TABLE SOLDIER (
    id INTEGER PRIMARY KEY,
    is_alive BOOLEAN NOT NULL,
    name TEXT NOT NULL,
    surname TEXT NOT NULL,
    birth_date DATE NOT NULL,
    soldier_rank TEXT NOT NULL CHECK (
        soldier_rank IN (
            'Private (E-1)', 'Private (E-2)', 'Private First Class (E-3)',
            'Specialist / Corporal (E-4)', 'Sergeant (E-5)', 'Staff Sergeant (E-6)',
            'Sergeant First Class (E-7)', 'Master Sergeant / First Sergeant (E-8)',
            'Sergeant Major / Command Sergeant Major (E-9)', 'Warrant Officer 1 (W-1)',
            'Chief Warrant Officer 2 (W-2)', 'Chief Warrant Officer 3 (W-3)',
            'Chief Warrant Officer 4 (W-4)', 'Chief Warrant Officer 5 (W-5)',
            'Second Lieutenant (O-1)', 'First Lieutenant (O-2)', 'Captain (O-3)',
            'Major (O-4)', 'Lieutenant Colonel (O-5)', 'Colonel (O-6)',
            'Brigadier General (O-7)', 'Major General (O-8)', 'Lieutenant General (O-9)',
            'General (O-10)', 'General of the Army (O-11)'
        )
    ),
    veteran_status BOOLEAN,
    division_id INTEGER,
    FOREIGN KEY (division_id) REFERENCES DIVISION(id)
);

-- Insert trigger
CREATE TRIGGER soldier_ai AFTER INSERT ON SOLDIER BEGIN
  INSERT INTO SOLDIER_FTS(rowid, name, surname, soldier_rank)
  VALUES (new.id, new.name, new.surname, new.soldier_rank);
END;

-- Update trigger
CREATE TRIGGER soldier_au AFTER UPDATE ON SOLDIER BEGIN
  UPDATE SOLDIER_FTS SET
    name = new.name,
    surname = new.surname,
    soldier_rank = new.soldier_rank
  WHERE rowid = new.id;
END;

-- Delete trigger
CREATE TRIGGER soldier_ad AFTER DELETE ON SOLDIER BEGIN
  DELETE FROM SOLDIER_FTS WHERE rowid = old.id;
END;

CREATE TABLE SOLDIER_FAMILY (
    id INTEGER PRIMARY KEY,
    related_soldier_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    surname TEXT NOT NULL,
    relation_type TEXT NOT NULL CHECK (relation_type IN ('Spouse', 'Parent', 'Child', 'Sibling', 'Other')),
    phone_number TEXT NOT NULL,
    FOREIGN KEY (related_soldier_id) REFERENCES SOLDIER (id)
);

-- Insert trigger
CREATE TRIGGER soldier_family_ai AFTER INSERT ON SOLDIER_FAMILY BEGIN
  INSERT INTO SOLDIER_FAMILY_FTS(rowid, name, surname, relation_type, phone_number)
  VALUES (new.id, new.name, new.surname, new.relation_type, new.phone_number);
END;

-- Update trigger
CREATE TRIGGER soldier_family_au AFTER UPDATE ON SOLDIER_FAMILY BEGIN
  UPDATE SOLDIER_FAMILY_FTS SET
    name = new.name,
    surname = new.surname,
    relation_type = new.relation_type,
		phone_number = new.phone_number
  WHERE rowid = new.id;
END;

-- Delete trigger
CREATE TRIGGER soldier_family_ad AFTER DELETE ON SOLDIER_FAMILY BEGIN
  DELETE FROM SOLDIER_FAMILY_FTS WHERE rowid = old.id;
END;

CREATE TABLE BASE (
    id INTEGER PRIMARY KEY,
    commander_id INTEGER,
    base_name TEXT NOT NULL,
    base_expenses REAL NOT NULL,
    base_capacity INTEGER NOT NULL,
    FOREIGN KEY (commander_id) REFERENCES SOLDIER (id)
);

-- Insert trigger
CREATE TRIGGER base_ai AFTER INSERT ON BASE BEGIN
  INSERT INTO BASE_FTS(rowid, base_name)
  VALUES (new.id, new.base_name);
END;

-- Update trigger
CREATE TRIGGER base_au AFTER UPDATE ON BASE BEGIN
  UPDATE BASE_FTS SET
    base_name = new.base_name
  WHERE rowid = new.id;
END;

-- Delete trigger
CREATE TRIGGER base_ad AFTER DELETE ON BASE BEGIN
  DELETE FROM BASE_FTS WHERE rowid = old.id;
END;

CREATE TABLE VEHICLE (
    id INTEGER PRIMARY KEY,
    vehicle_type TEXT NOT NULL CHECK (vehicle_type IN ('TANK', 'JEEP', 'TRUCK', 'HELICOPTER', 'AIRPLANE', 'ARMORED CAR', 'MOTORCYCLE', 'DUNE BUGGY', 'SUBMARINE', 'DESTROYER', 'AMPHIBIOUS VEHICLE', 'DRONE')),
    is_operational BOOLEAN NOT NULL,
    damage_expenses REAL,
    base_id INTEGER,
    model TEXT NOT NULL,
    license_plate TEXT NOT NULL,
    mission_id INTEGER,
    FOREIGN KEY (base_id) REFERENCES BASE (id),
    FOREIGN KEY (mission_id) REFERENCES MISSION (id)
);

-- Insert trigger
CREATE TRIGGER vehicle_ai AFTER INSERT ON VEHICLE BEGIN
  INSERT INTO VEHICLE_FTS(rowid, vehicle_type, model, license_plate)
  VALUES (new.id, new.vehicle_type, new.model, new.license_plate);
END;

-- Update trigger
CREATE TRIGGER vehicle_au AFTER UPDATE ON VEHICLE BEGIN
  UPDATE VEHICLE_FTS SET
    vehicle_type = new.vehicle_type,
    model = new.model,
    license_plate = new.license_plate
  WHERE rowid = new.id;
END;

-- Delete trigger
CREATE TRIGGER vehicle_ad AFTER DELETE ON VEHICLE BEGIN
  DELETE FROM VEHICLE_FTS WHERE rowid = old.id;
END;

CREATE TABLE DIVISION (
    id INTEGER PRIMARY KEY,
    commander_id INTEGER,
    quota INTEGER NOT NULL,
    division_name TEXT NOT NULL,
    base_id INTEGER,
    super_division_id INTEGER,
    FOREIGN KEY (commander_id) REFERENCES SOLDIER (id),
    FOREIGN KEY (base_id) REFERENCES BASE (id),
    FOREIGN KEY (super_division_id) REFERENCES DIVISION (id)
);

-- Insert trigger
CREATE TRIGGER division_ai AFTER INSERT ON DIVISION BEGIN
  INSERT INTO DIVISION_FTS(rowid, division_name)
  VALUES (new.id, new.division_name);
END;

-- Update trigger
CREATE TRIGGER division_au AFTER UPDATE ON DIVISION BEGIN
  UPDATE DIVISION_FTS SET
    division_name = new.division_name
  WHERE rowid = new.id;
END;

-- Delete trigger
CREATE TRIGGER division_ad AFTER DELETE ON DIVISION BEGIN
  DELETE FROM DIVISION_FTS WHERE rowid = old.id;
END;

CREATE TABLE MISSION (
    id INTEGER PRIMARY KEY,
    mission_name TEXT NOT NULL,
    division_id INTEGER,
    start_date DATE NOT NULL,
    end_date DATE,
    mission_type TEXT NOT NULL CHECK (mission_type IN ('reconnaissance', 'assault', 'defensive', 'rescue', 'training')),
    operation_id INTEGER,
    FOREIGN KEY (division_id) REFERENCES DIVISION (id),
    FOREIGN KEY (operation_id) REFERENCES OPERATION (id)
);

-- Insert trigger
CREATE TRIGGER mission_ai AFTER INSERT ON MISSION BEGIN
  INSERT INTO MISSION_FTS(rowid, mission_name, mission_type)
  VALUES (new.id, new.mission_name, new.mission_type);
END;

-- Update trigger
CREATE TRIGGER mission_au AFTER UPDATE ON MISSION BEGIN
  UPDATE MISSION_FTS SET
    mission_name = new.mission_name,
    mission_type = new.mission_type
  WHERE rowid = new.id;
END;

-- Delete trigger
CREATE TRIGGER mission_ad AFTER DELETE ON MISSION BEGIN
  DELETE FROM MISSION_FTS WHERE rowid = old.id;
END;

CREATE TABLE OPERATION (
    id INTEGER PRIMARY KEY,
    operation_name TEXT NOT NULL,
    operation_type TEXT NOT NULL CHECK (operation_type IN ('offensive', 'defensive', 'peacekeeping', 'rescue', 'training')),
    division_id INTEGER,
    start_date DATE NOT NULL,
    end_date DATE,
    FOREIGN KEY (division_id) REFERENCES DIVISION (id)
);

-- Insert trigger
CREATE TRIGGER operation_ai AFTER INSERT ON OPERATION BEGIN
  INSERT INTO OPERATION_FTS(rowid, operation_name, operation_type)
  VALUES (new.id, new.operation_name, new.operation_type);
END;

-- Update trigger
CREATE TRIGGER operation_au AFTER UPDATE ON OPERATION BEGIN
  UPDATE OPERATION_FTS SET
    operation_name = new.operation_name,
    operation_type = new.operation_type
  WHERE rowid = new.id;
END;

-- Delete trigger
CREATE TRIGGER operation_ad AFTER DELETE ON OPERATION BEGIN
  DELETE FROM OPERATION_FTS WHERE rowid = old.id;
END;

CREATE TABLE MILITARY_EVENT (
    id INTEGER PRIMARY KEY,
    event_title TEXT NOT NULL,
    event_description TEXT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    operation_id INTEGER,
    mission_id INTEGER,
    FOREIGN KEY (operation_id) REFERENCES OPERATION (id),
    FOREIGN KEY (mission_id) REFERENCES MISSION (id)
);

-- Insert trigger
CREATE TRIGGER military_event_ai AFTER INSERT ON MILITARY_EVENT BEGIN
  INSERT INTO MILITARY_EVENT_FTS(rowid, event_title, event_description)
  VALUES (new.id, new.event_title, new.event_description);
END;

-- Update trigger
CREATE TRIGGER military_event_au AFTER UPDATE ON MILITARY_EVENT BEGIN
  UPDATE MILITARY_EVENT_FTS SET
    event_title = new.event_title,
    event_description = new.event_description
  WHERE rowid = new.id;
END;

-- Delete trigger
CREATE TRIGGER military_event_ad AFTER DELETE ON MILITARY_EVENT BEGIN
  DELETE FROM MILITARY_EVENT_FTS WHERE rowid = old.id;
END;