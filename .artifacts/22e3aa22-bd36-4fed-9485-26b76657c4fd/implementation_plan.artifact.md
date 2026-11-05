# Add Table Schedule Feature

This plan adds a new feature to create a university class timetable using a grid/table, as an alternative to uploading an image.

## User Review Required

> [!IMPORTANT]
> The table data will be persisted using `SharedPreferences`. If the user has a lot of data, this might be slightly less efficient than a database, but for a simple university schedule, it should be sufficient.
> The UI will allow removing day columns (Saturday to Wednesday). A way to restore them might be needed if the user accidentally deletes one. I will add a "Reset Days" option.

## Proposed Changes

### UI Components

#### [MODIFY] [MainActivity.kt](file:///D:/MyUni/app/src/main/java/com/example/myuni/MainActivity.kt)
- Add state for `useTableSchedule` (Boolean).
- Add state for `tableData` (Map<String, String>).
- Add state for `visibleDays` (List<String>).
- Implement `ScheduleTable` composable.
- Update `WebViewApp` to toggle between `Image` and `Table` views.
- Update `showOptionsDialog` to include an option for "Create Table".

## Verification Plan

### Manual Verification
- Deploy the app.
- Click on the schedule area.
- Choose "Create Table" (new option).
- Verify the table displays Saturday to Wednesday.
- Verify times 08:00 to 18:00 are shown in the first column.
- Type in a cell and verify it's saved (restart app).
- Delete a day column and verify it disappears.
- Switch back to Image mode and verify the image still works.
