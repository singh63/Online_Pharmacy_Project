INSERT INTO categories (category_name, image)
VALUES ('Drugs', '/drugs.png'),
       ('Injection', '/injection.png'),
       ('Cosmetic', '/sun-block.png'),
       ('Homeo', '/homeopathy.png'),
       ('Pain-relief', '/pain-relief.png'),
       ('Devices', '/lab-equipment.png'),
       ('Vitamins', '/multivitamin.png'),
       ('First-Aid', '/first-aid-kit.png');

INSERT INTO products (description, image, price, product_name, quantity, category_id)
VALUES ('A variety of medications for various ailments', '/drugs.png', 29.99, 'Aspirin', 100, 1),
       ('Used for injections of various medications', '/injection.png', 15.99, 'Syringe', 50, 2),
       ('Skin protection from the sun', '/sun-block.png', 19.99, 'Sun Block', 80,  3),
       ('Homeopathic remedies for common issues', '/homeopathy.png', 25.99, 'Homeo Remedy', 60, 4),
       ('Relieves various types of pain', '/pain-relief.png', 9.99, 'Pain Relief Tablet', 200, 5),
       ('Equipment for laboratory use', '/lab-equipment.png', 150.00, 'Microscope', 10, 6),
       ('Multivitamins for overall health', '/multivitamin.png', 29.99, 'Daily Multivitamin', 90, 7),
       ('Emergency medical kit for first aid', '/first-aid-kit.png', 49.99, 'First Aid Kit', 30, 8);



INSERT INTO products (description, image, price, product_name, quantity, category_id)
VALUES ('Antibiotic for bacterial infections', '/antibiotic.png', 20.99, 'Antibiotic', 150, 1),
       ('Pain relief medication for headaches', '/headache-relief.png', 12.99, 'Headache Relief', 200, 1),
       ('Vitamins to boost immune system', '/immune-vitamins.png', 25.99, 'Immune Booster', 100, 1),
       ('Cold and flu relief medication', '/cold-flu.png', 14.99, 'Cold & Flu Relief', 180, 1),
       ('Anti-inflammatory drug for arthritis', '/arthritis-relief.png', 22.99, 'Arthritis Relief', 80, 1),
       ('Medication for high blood pressure', '/blood-pressure.png', 30.99, 'Blood Pressure Med', 120, 1);



INSERT INTO products (description, image, price, product_name, quantity, category_id)
VALUES ('Syringe for vaccinations', '/vaccination-syringe.png', 10.99, 'Vaccination Syringe', 60, 2),
       ('Needle for injections', '/injection-needle.png', 5.99, 'Injection Needle', 100, 2),
       ('Syringe for insulin', '/insulin-syringe.png', 12.99, 'Insulin Syringe', 80, 2),
       ('Disposable syringes', '/disposable-syringe.png', 8.99, 'Disposable Syringe', 70, 2),
       ('Syringe for blood collection', '/blood-collection.png', 14.99, 'Blood Collection Syringe', 50, 2),
       ('Syringe for IV administration', '/iv-syringe.png', 18.99, 'IV Administration Syringe', 40, 2);


INSERT INTO products (description, image, price, product_name, quantity, category_id)
VALUES ('Hydrating facial cream', '/facial-cream.png', 35.99, 'Facial Cream', 100, 3),
       ('Anti-aging serum', '/anti-aging-serum.png', 40.99, 'Anti-Aging Serum', 90, 3),
       ('Nourishing hair conditioner', '/hair-conditioner.png', 25.99, 'Hair Conditioner', 150, 3),
       ('Moisturizing body lotion', '/body-lotion.png', 19.99, 'Body Lotion', 200, 3),
       ('Refreshing face mist', '/face-mist.png', 22.99, 'Face Mist', 130, 3),
       ('Brightening eye cream', '/eye-cream.png', 28.99, 'Eye Cream', 110, 3);



INSERT INTO products (description, image, price, product_name, quantity, category_id)
VALUES ('Homeopathic remedy for colds', '/homeo-cold.png', 18.99, 'Cold Remedy', 90, 4),
       ('Homeopathic treatment for allergies', '/homeo-allergy.png', 22.99, 'Allergy Treatment', 70, 4),
       ('Homeopathic medicine for sleep', '/homeo-sleep.png', 27.99, 'Sleep Medicine', 80, 4),
       ('Homeopathic cure for stress', '/homeo-stress.png', 32.99, 'Stress Cure', 60, 4),
       ('Homeopathic remedy for digestion', '/homeo-digestion.png', 15.99, 'Digestion Remedy', 100, 4),
       ('Homeopathic treatment for skin issues', '/homeo-skin.png', 19.99, 'Skin Treatment', 85, 4);


INSERT INTO products (description, image, price, product_name, quantity, category_id)
VALUES ('Pain relief patches', '/pain-patch.png', 12.99, 'Pain Relief Patch', 150, 5),
       ('Muscle relaxant gel', '/muscle-gel.png', 18.99, 'Muscle Gel', 120, 5),
       ('Pain relief cream', '/pain-cream.png', 24.99, 'Pain Relief Cream', 80, 5),
       ('Anti-inflammatory tablets', '/anti-inflammatory.png', 14.99, 'Anti-Inflammatory Tabs', 100, 5),
       ('Back pain relief spray', '/back-pain-spray.png', 19.99, 'Back Pain Spray', 90, 5),
       ('Pain relief ointment', '/pain-ointment.png', 22.99, 'Pain Ointment', 110, 5);


INSERT INTO products (description, image, price, product_name, quantity, category_id)
VALUES ('Digital thermometer', '/thermometer.png', 12.99, 'Thermometer', 100, 6),
       ('Blood pressure monitor', '/bp-monitor.png', 29.99, 'BP Monitor', 80, 6),
       ('Pulse oximeter', '/pulse-oximeter.png', 18.99, 'Pulse Oximeter', 60, 6),
       ('Medical stethoscope', '/stethoscope.png', 45.99, 'Stethoscope', 50, 6),
       ('Glucose meter', '/glucose-meter.png', 22.99, 'Glucose Meter', 70, 6),
       ('Nebulizer', '/nebulizer.png', 59.99, 'Nebulizer', 40, 6);


INSERT INTO products (description, image, price, product_name, quantity, category_id)
VALUES ('Multivitamins for adults', '/adult-multivitamins.png', 30.99, 'Adult Multivitamins', 90, 7),
       ('Vitamin C tablets', '/vitamin-c.png', 19.99, 'Vitamin C', 100, 7),
       ('Vitamin D capsules', '/vitamin-d.png', 22.99, 'Vitamin D', 80, 7),
       ('Omega-3 supplements', '/omega3.png', 25.99, 'Omega-3', 70, 7),
       ('Multivitamins for kids', '/kids-multivitamins.png', 18.99, 'Kids Multivitamins', 120, 7),
       ('Vitamin B complex', '/vitamin-b.png', 27.99, 'Vitamin B Complex', 85, 7);


INSERT INTO products (description, image, price, product_name, quantity, category_id)
VALUES ('First aid bandage roll', '/bandage-roll.png', 8.99, 'Bandage Roll', 200, 8),
       ('Antiseptic wipes', '/antiseptic-wipes.png', 5.99, 'Antiseptic Wipes', 150, 8),
       ('Medical adhesive tape', '/adhesive-tape.png', 4.99, 'Adhesive Tape', 180, 8),
       ('Instant cold packs', '/cold-packs.png', 6.99, 'Cold Packs', 120, 8),
       ('Burn ointment', '/burn-ointment.png', 9.99, 'Burn Ointment', 140, 8),
       ('First aid scissors', '/first-aid-scissors.png', 12.99, 'First Aid Scissors', 90, 8);
